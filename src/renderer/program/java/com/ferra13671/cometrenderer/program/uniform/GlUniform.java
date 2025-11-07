package com.ferra13671.cometrenderer.program.uniform;

import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.uniform.uniforms.sampler.SamplerUniform;

/**
 * Объект, представляющий собой параметр программы ({@link GlProgram}), предназначенный для передачи различных параметров для настройки обработки пикселей программой.
 * <p>
 * Может представлять собой как и обычную униформу, так и семплер ({@link SamplerUniform}) (униформа, передающая изображение)
 * <p>
 * Данные в униформе нельзя перезаписать в программе, поэтому они могут использоваться бесконечно до перезаписи через CPU.
 *
 * @see <a href="https://wikis.khronos.org/opengl/Uniform_(GLSL)">OpenGL uniform wiki</a>
 */
//TODO загрузка данных в униформу независимо от бинда программы.
public abstract class GlUniform {
    /** Имя униформы. **/
    protected final String name;
    /** Локация униформы в OpenGL. **/
    protected final int location;
    /** Программа ({@link GlProgram}), к которой привязана униформа. **/
    protected final GlProgram glProgram;

    /**
     * @param name имя униформы.
     * @param location локация униформы в OpenGL.
     * @param glProgram программа ({@link GlProgram}), к которой привязана униформа.
     */
    public GlUniform(String name, int location, GlProgram glProgram) {
        this.name = name;
        this.location = location;
        this.glProgram = glProgram;
    }

    /**
     * Возвращает имя униформы.
     *
     * @return имя униформы.
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает локацию униформы в OpenGL.
     *
     * @return локация униформы в OpenGL.
     */
    public int getLocation() {
        return location;
    }

    /**
     * Возвращает программу, к которой привязана униформа.
     *
     * @return программа, к которой привязана униформа.
     * @see GlProgram
     */
    public GlProgram getGlProgram() {
        return glProgram;
    }

    /**
     * Загружает данные в униформу.
     */
    public abstract void upload();
}
