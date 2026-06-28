package com.ferra13671.cometrenderer.glsl;

import com.ferra13671.cometrenderer.ErrorHandlers;
import com.ferra13671.cometrenderer.State;
import com.ferra13671.cometrenderer.glsl.compiler.CometCompiler;
import com.ferra13671.cometrenderer.utils.Bindable;
import com.ferra13671.cometrenderer.glsl.uniform.GLUniform;
import com.ferra13671.cometrenderer.glsl.uniform.uniforms.OneTypeGLUniform;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import com.ferra13671.cometrenderer.glsl.uniform.uniforms.SamplerUniform;
import com.ferra13671.cometrenderer.glsl.shader.GLShader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apiguardian.api.API;
import org.lwjgl.opengl.GL20;

import java.io.Closeable;
import java.util.*;
import java.util.function.Consumer;

/**
 * Объект, хранящий в себе скомпилированный набор шейдеров ({@link GLShader}), готовых к использованию для отрисовки вершин в GPU.
 * <p>
 * Основной набор шейдеров в программе: вершинный(Vertex) и фрагментный(Fragment).
 * Есть также дополнительные шейдеры, которые могут входить в программу: геометрический, тесселяционные и compute.
 * <p>
 * Программа также может иметь в себе униформы ({@link GLUniform}), предназначенные для передачи различных параметров для настройки обработки пикселей программой.
 *
 * @see CometCompiler
 */
@API(status = API.Status.MAINTAINED, since = "1.1")
@RequiredArgsConstructor
public class GLProgram implements Bindable, Closeable {
    /** Имя программы. **/
    @Getter
    private final String name;
    /** Айди программы в OpenGL. **/
    @Getter
    private final int id;
    /** Фрагменты программы, добавленные в программу. **/
    @Getter
    private final Set<GLProgramSnippet> snippets;
    /** Карта всех униформ программы, расположенных по их именам. **/
    private final Map<String, GLUniform> uniformsByName;
    /** Список всех семплеров программы. **/
    private final List<SamplerUniform> samplers;
    /** Список униформ, которые были обновлены. Данный список нужен для того, что бы повторно загружать в GPU только те униформы, которые были обновлены. **/
    private final List<GLUniform> updatedUniforms = new ArrayList<>();

    @Override
    public void close() {
        GL20.glDeleteProgram(getId());

        this.uniformsByName.clear();
        this.samplers.clear();
    }

    /**
     * Устанавливает данную программу в OpenGL как активную в данный момент.
     */
    @Override
    public void bind() {
        State.PROGRAM.bind(getId());

        if (!this.updatedUniforms.isEmpty()) {
            for (GLUniform glUniform : this.updatedUniforms)
                glUniform.upload();
            this.updatedUniforms.clear();
        }
    }

    /**
     * Устанавливает активную программу в OpenGL как 0 (т.е. без активной программы)
     */
    @Override
    public void unbind() {
        State.PROGRAM.bind(0);
    }

    /**
     * Добавляет в список программы униформу, которая была обновлена.
     * Данный метод должен всегда вызываться в реализации униформы в том случае, когда данные в них были обновлены, что бы изменения были загружены на GPU.
     *
     * @param uniform униформа, которая была обновлена.
     *
     * @see GLUniform
     * @see OneTypeGLUniform
     */
    @API(status = API.Status.INTERNAL)
    public void addUpdatedUniform(GLUniform uniform) {
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
     * @see GLUniform
     * @see UniformType
     */
    @API(status = API.Status.STABLE, since = "1.6")
    public <T extends GLUniform> T getUniform(String name, UniformType<T> type) {
        T uniform = getUniformNullable(name, type);
        if (uniform == null)
            ErrorHandlers.onNoSuchUniform(name, this.name);
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
     * @see GLUniform
     * @see UniformType
     */
    @API(status = API.Status.STABLE, since = "1.6")
    public <T extends GLUniform> T getUniformNullable(String name, UniformType<T> type) {
        return (T) this.uniformsByName.get(name);
    }

    /**
     * Если требуемая униформа существует в программе, то будет выполнен данный метод с ней.
     *
     * @param name имя требуемой униформы.
     * @param type тип требуемой униформы.
     * @param consumer метод, который выполнится в том случае, если требуемая униформа существует в программе.
     * @param <T> униформа.
     *
     * @see GLUniform
     * @see UniformType
     */
    @API(status = API.Status.MAINTAINED, since = "1.8.3")
    public <T extends GLUniform> void consumeIfUniformPresent(String name, UniformType<T> type, Consumer<T> consumer) {
        T uniform = getUniformNullable(name, type);
        if (uniform != null)
            consumer.accept(uniform);
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
    @API(status = API.Status.MAINTAINED, since = "1.5")
    public SamplerUniform getSampler(int samplerId) {
        SamplerUniform sampler = getSamplerNullable(samplerId);
        if (sampler == null)
            ErrorHandlers.onNoSuchUniform("Sampler[" + samplerId + "]", this.name);
        return sampler;
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
    @API(status = API.Status.MAINTAINED, since = "1.8.3")
    public SamplerUniform getSamplerNullable(int samplerId) {
        return samplerId < 0 || samplerId > this.samplers.size() - 1 ? null : this.samplers.get(samplerId);
    }

    /**
     * Если требуемый семплер существует в программе, то будет выполнен данный метод с ним.
     *
     * @param samplerId айди требуемого семплера.
     * @param consumer метод, который выполнится в том случае, если требуемый семплер существует в программе.
     *
     * @see SamplerUniform
     */
    @API(status = API.Status.MAINTAINED, since = "1.8.3")
    public void consumerIfSamplerPresent(int samplerId, Consumer<SamplerUniform> consumer) {
        SamplerUniform sampler = getSamplerNullable(samplerId);
        if (sampler != null)
            consumer.accept(sampler);
    }
}