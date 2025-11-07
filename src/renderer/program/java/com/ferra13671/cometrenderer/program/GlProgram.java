package com.ferra13671.cometrenderer.program;

import com.ferra13671.cometrenderer.Bindable;
import com.ferra13671.cometrenderer.Compilable;
import com.ferra13671.cometrenderer.exceptions.ExceptionPrinter;
import com.ferra13671.cometrenderer.exceptions.impl.NoSuchUniformException;
import com.ferra13671.cometrenderer.builders.GlUniformSchema;
import com.ferra13671.cometrenderer.program.compile.CompileResult;
import com.ferra13671.cometrenderer.program.compile.CompileStatus;
import com.ferra13671.cometrenderer.program.uniform.GlUniform;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import com.ferra13671.cometrenderer.program.uniform.uniforms.buffer.BufferUniform;
import com.ferra13671.cometrenderer.program.uniform.uniforms.sampler.SamplerUniform;
import com.ferra13671.cometrenderer.program.shader.GlShader;
import com.ferra13671.cometrenderer.compile.GlobalCometCompiler;
import com.ferra13671.ferraguard.annotations.OverriddenMethod;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL20;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Объект, хранящий в себе скомпилированный набор шейдеров ({@link GlShader}), готовых к использованию для отрисовки вершин в GPU.
 * <p>
 * Основной набор шейдеров в программе: вершинный(Vertex) и фрагментный(Fragment).
 * Есть также дополнительные шейдеры, которые могут входить в программу: геометрический, тесселяционные и compute.
 * <p>
 * Программа также может иметь в себе униформы ({@link GlUniform}), предназначенные для передачи различных параметров для настройки обработки пикселей программой.
 *
 * @see GlobalCometCompiler
 */
//TODO setUniformIfPresent
//TODO setSamplerIfPresent
//TODO getProgramSnippets
public class GlProgram implements Bindable, Compilable, Closeable {
    public static GlProgram ACTIVE_PROGRAM = null;

    /** Имя программы. **/
    private final String name;
    /** Айди программы в OpenGL. **/
    private final int id;
    /** Список всех униформ программы. **/
    private final List<GlUniform> uniforms = new ArrayList<>();
    /** Карта всех униформ программы, расположенных по их именам. **/
    private final HashMap<String, GlUniform> uniformsByName = new HashMap<>();
    /** Список всех семплеров программы. **/
    private final List<SamplerUniform> samplers = new ArrayList<>();
    /** Количество всех семплеров программы. **/
    private int samplersAmount = 0;
    /** Количество привязанных индексов буфферов для униформ с типом BUFFER. **/
    private int buffersIndexAmount = 0;

    /**
     * @param name имя программы.
     * @param id айди программы в OpenGL.
     * @param uniforms список всех униформ программы.
     */
    public GlProgram(String name, int id, List<GlUniformSchema<?>> uniforms) {
        this.name = name;
        this.id = id;

        for (GlUniformSchema<?> glUniformSchema : uniforms) {
            GlUniform uniform = glUniformSchema.uniformType().uniformCreator().apply(
                    glUniformSchema.name(),
                    glUniformSchema.getLocationFromProgram(this.id),
                    this
            );

            if (uniform.getLocation() == -1 && !(uniform instanceof BufferUniform))
                ExceptionPrinter.printAndExit(new NoSuchUniformException(uniform.getName(), this.name));

            this.uniforms.add(uniform);
            this.uniformsByName.put(glUniformSchema.name(), uniform);

            if (uniform instanceof SamplerUniform sampler)
                samplers.add(sampler);
        }
    }

    @Override
    @OverriddenMethod
    public CompileResult getCompileResult() {
        CompileStatus status = CompileStatus.fromStatusId(GL20.glGetProgrami(getId(), GL20.GL_LINK_STATUS));
        return new CompileResult(
                status,
                status == CompileStatus.FAILURE ? StringUtils.trim(GL20.glGetProgramInfoLog(getBuffersIndexAmount())) : ""
        );
    }

    @Override
    @OverriddenMethod
    public void close() {
        GL20.glDeleteProgram(getId());
        this.uniforms.clear();
        this.uniformsByName.clear();
        this.samplers.clear();
    }

    /**
     * Устанавливает данную программу в OpenGL как активную в данный момент.
     */
    @Override
    @OverriddenMethod
    public void bind() {
        GL20.glUseProgram(getId());

        for (GlUniform glUniform : uniforms)
            glUniform.upload();

        ACTIVE_PROGRAM = this;
    }

    /**
     * Устанавливает активную программу в OpenGL как 0 (т.е. без активной программы)
     */
    @Override
    @OverriddenMethod
    public void unbind() {
        GL20.glUseProgram(0);

        ACTIVE_PROGRAM = null;
    }

    /**
     * Возвращает имя программы.
     *
     * @return имя программы.
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает айди программы в OpenGL.
     *
     * @return айди программы в OpenGL.
     */
    public int getId() {
        return id;
    }

    /**
     * Возвращает униформу программы с данным именем и типом.
     * Если униформа с данной конфигурацией не существует в программе, то метод вызовет ошибку.
     *
     * @param name имя требуемой униформы.
     * @param type тип требуемой униформы.
     * @param <T> униформа.
     * @return требуемая униформа.
     *
     * @see GlUniform
     * @see UniformType
     */
    public <T extends GlUniform> T getUniform(String name, UniformType<T> type) {
        T uniform = getUniformNullable(name, type);
        if (uniform == null)
            ExceptionPrinter.printAndExit(new NoSuchUniformException(name, this.name));
        return uniform;
    }

    /**
     * Возвращает униформу программы с данным именем и типом.
     * Если униформа с данной конфигурацией не существует в программе, то метод вернет null.
     *
     * @param name имя требуемой униформы.
     * @param type тип требуемой униформы.
     * @param <T> униформа.
     * @return требуемая униформа либо null, если таковая не была найдена.
     *
     * @see GlUniform
     * @see UniformType
     */
    public <T extends GlUniform> T getUniformNullable(String name, UniformType<T> type) {
        return (T) uniformsByName.get(name);
    }

    /**
     * Возвращает семплер программы с данным айди.
     * Если семплер с данным айди не существует в программе, то метод вызовет ошибку.
     *
     * @param samplerId айди требуемого семплера.
     * @return требуемый семплер.
     *
     * @see SamplerUniform
     */
    //TODO getSamplerNullable
    public SamplerUniform getSampler(int samplerId) {
        SamplerUniform sampler = samplers.get(samplerId);
        if (sampler == null)
            ExceptionPrinter.printAndExit(new NoSuchUniformException("Sampler[" + samplerId + "]", this.name));
        return sampler;
    }

    /**
     * Возвращает количество привязанных индексов буфферов для униформ с типом BUFFER.
     *
     * @return количество привязанных индексов буфферов для униформ с типом BUFFER.
     *
     * @see BufferUniform
     */
    public int getBuffersIndexAmount() {
        return buffersIndexAmount;
    }

    /**
     * Устанавливает количество привязанных индексов буфферов для униформ с типом BUFFER.
     *
     * @param buffersIndexAmount количество привязанных индексов буфферов для униформ с типом BUFFER.
     *
     * @see BufferUniform
     */
    public void setBuffersIndexAmount(int buffersIndexAmount) {
        this.buffersIndexAmount = buffersIndexAmount;
    }

    /**
     * Возвращает количество всех семплеров программы.
     *
     * @return количество всех семплеров программы.
     *
     * @see SamplerUniform
     */
    public int getSamplersAmount() {
        return samplersAmount;
    }

    /**
     * Устанавливает количество всех семплеров программы.
     *
     * @param samplersAmount количество всех семплеров программы.
     *
     * @see SamplerUniform
     */
    public void setSamplersAmount(int samplersAmount) {
        this.samplersAmount = samplersAmount;
    }
}