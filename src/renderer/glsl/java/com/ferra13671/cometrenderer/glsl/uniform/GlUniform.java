package com.ferra13671.cometrenderer.glsl.uniform;

import com.ferra13671.cometrenderer.glsl.GlProgram;
import com.ferra13671.cometrenderer.glsl.uniform.uniforms.SamplerUniform;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.apiguardian.api.API;

/**
 * Объект, представляющий собой параметр программы ({@link GlProgram}), предназначенный для передачи различных параметров для настройки обработки пикселей программой.
 * <p>
 * Может представлять собой как и обычную униформу, так и семплер ({@link SamplerUniform}) (униформа, передающая изображение)
 * <p>
 * Данные в униформе нельзя перезаписать в программе, поэтому они могут использоваться бесконечно до перезаписи через CPU.
 *
 * @see <a href="https://wikis.khronos.org/opengl/Uniform_(GLSL)">OpenGL uniform wiki</a>
 */
@Getter
@AllArgsConstructor
@API(status = API.Status.STABLE, since = "1.1")
public abstract class GlUniform {
    /** Имя униформы. **/
    @NonNull
    protected final String name;
    /** Локация униформы в OpenGL. **/
    @API(status = API.Status.INTERNAL)
    protected final int location;
    /** Программа ({@link GlProgram}), к которой привязана униформа. **/
    @NonNull
    @API(status = API.Status.INTERNAL)
    protected final GlProgram program;

    /**
     * Загружает данные в униформу.
     */
    @API(status = API.Status.INTERNAL)
    public abstract void upload();
}
