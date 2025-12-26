package com.ferra13671.cometrenderer.program;

import com.ferra13671.cometrenderer.utils.Bindable;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.utils.Compilable;
import com.ferra13671.cometrenderer.exceptions.impl.NoSuchUniformException;
import com.ferra13671.cometrenderer.program.compile.CompileResult;
import com.ferra13671.cometrenderer.program.compile.CompileStatus;
import com.ferra13671.cometrenderer.program.uniform.GlUniform;
import com.ferra13671.cometrenderer.program.uniform.uniforms.OneTypeGlUniform;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import com.ferra13671.cometrenderer.program.uniform.uniforms.BufferUniform;
import com.ferra13671.cometrenderer.program.uniform.uniforms.SamplerUniform;
import com.ferra13671.cometrenderer.program.shader.GlShader;
import com.ferra13671.cometrenderer.compiler.GlobalCometCompiler;
import com.ferra13671.ferraguard.annotations.OverriddenMethod;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.opengl.GL20;

import java.io.Closeable;
import java.util.*;
import java.util.function.Consumer;

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
public class GlProgram implements Bindable, Compilable, Closeable {
    /** Имя программы. **/
    @Getter
    private final String name;
    /** Айди программы в OpenGL. **/
    @Getter
    private final int id;
    /** Фрагменты программы, добавленные в программу. **/
    @Getter
    private final HashSet<GlProgramSnippet> snippets;
    /** Карта всех униформ программы, расположенных по их именам. **/
    private final HashMap<String, GlUniform> uniformsByName = new HashMap<>();
    /** Список всех семплеров программы. **/
    private final List<SamplerUniform> samplers = new ArrayList<>();
    /** Количество всех семплеров программы. **/
    @Getter
    @Setter
    private int samplersAmount = 0;
    /** Количество привязанных индексов буфферов для униформ с типом BUFFER. **/
    @Getter
    @Setter
    private int buffersIndexAmount = 0;
    /** Список униформ, которые были обновлены. Данный список нужен для того, что бы повторно загружать в GPU только те униформы, которые были обновлены. **/
    private final List<GlUniform> updatedUniforms = new ArrayList<>();

    /**
     * @param name имя программы.
     * @param id айди программы в OpenGL.
     * @param uniforms список всех униформ программы.
     */
    public GlProgram(String name, int id, HashSet<GlProgramSnippet> snippets, Map<String, UniformType<?>> uniforms) {
        this.name = name;
        this.id = id;
        this.snippets = snippets;

        for (Map.Entry<String, UniformType<?>> uniformEntry : uniforms.entrySet()) {
            GlUniform uniform = uniformEntry.getValue().uniformCreator().apply(
                    uniformEntry.getKey(),
                    GL20.glGetUniformLocation(this.id, uniformEntry.getKey()),
                    this
            );

            if (uniform.getLocation() == -1 && !(uniform instanceof BufferUniform))
                CometRenderer.manageException(new NoSuchUniformException(uniform.getName(), this.name));

            this.uniformsByName.put(uniformEntry.getKey(), uniform);

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
                status == CompileStatus.FAILURE ? GL20.glGetProgramInfoLog(getBuffersIndexAmount()).trim() : ""
        );
    }

    @Override
    @OverriddenMethod
    public void close() {
        GL20.glDeleteProgram(getId());
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

        if (!this.updatedUniforms.isEmpty()) {
            for (GlUniform glUniform : this.updatedUniforms)
                glUniform.upload();
            this.updatedUniforms.clear();
        }
    }

    /**
     * Устанавливает активную программу в OpenGL как 0 (т.е. без активной программы)
     */
    @Override
    @OverriddenMethod
    public void unbind() {
        GL20.glUseProgram(0);
    }

    /**
     * Добавляет в список программы униформу, которая была обновлена.
     * Данный метод должен всегда вызываться в реализации униформы в том случае, когда данные в них были обновлены, что бы изменения были загружены на GPU.
     *
     * @param uniform униформа, которая была обновлена.
     *
     * @see GlUniform
     * @see OneTypeGlUniform
     */
    public void addUpdatedUniform(GlUniform uniform) {
        this.updatedUniforms.add(uniform);
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
            CometRenderer.manageException(new NoSuchUniformException(name, this.name));
        return uniform;
    }

    /**
     * Если требуемая униформа существует в программе, то будет выполнен данный метод с ней.
     *
     * @param name имя требуемой униформы.
     * @param type тип требуемой униформы.
     * @param consumer метод, который выполнится в том случае, если требуемая униформа существует в программе.
     * @param <T> униформа.
     *
     * @see GlUniform
     * @see UniformType
     */
    public <T extends GlUniform> void consumeIfUniformPresent(String name, UniformType<T> type, Consumer<T> consumer) {
        T uniform = getUniformNullable(name, type);
        if (uniform != null)
            consumer.accept(uniform);
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
    public SamplerUniform getSampler(int samplerId) {
        SamplerUniform sampler = getSamplerNullable(samplerId);
        if (sampler == null)
            CometRenderer.manageException(new NoSuchUniformException("Sampler[" + samplerId + "]", this.name));
        return sampler;
    }

    /**
     * Если требуемый семплер существует в программе, то будет выполнен данный метод с ним.
     *
     * @param samplerId айди требуемого семплера.
     * @param consumer метод, который выполнится в том случае, если требуемый семплер существует в программе.
     *
     * @see SamplerUniform
     */
    public void consumerIfSamplerPresent(int samplerId, Consumer<SamplerUniform> consumer) {
        SamplerUniform sampler = getSamplerNullable(samplerId);
        if (sampler != null)
            consumer.accept(sampler);
    }

    /**
     * Возвращает семплер программы с данным айди.
     * Если семплер с данным айди не существует в программе, то метод вернет null.
     *
     * @param samplerId айди требуемого семплера.
     * @return требуемый семплер либо null, если такового не было найдено.
     *
     * @see SamplerUniform
     */
    public SamplerUniform getSamplerNullable(int samplerId) {
        return this.samplers.get(samplerId);
    }
}