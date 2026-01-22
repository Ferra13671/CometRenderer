package com.ferra13671.cometrenderer.glsl.uniform;

import com.ferra13671.cometrenderer.glsl.GlProgram;
import com.ferra13671.cometrenderer.glsl.uniform.uniforms.SamplerUniform;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

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
public abstract class GlUniform {
    /** Имя униформы. **/
    @NonNull
    protected final String name;
    /** Локация униформы в OpenGL. **/
    protected final int location;
    /** Программа ({@link GlProgram}), к которой привязана униформа. **/
    @NonNull
    protected final GlProgram program;

    /**
     * Загружает данные в униформу.
     */
    public abstract void upload();
}
