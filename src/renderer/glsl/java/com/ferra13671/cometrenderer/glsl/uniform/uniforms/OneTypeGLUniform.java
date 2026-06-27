package com.ferra13671.cometrenderer.glsl.uniform.uniforms;

import com.ferra13671.cometrenderer.glsl.GLProgram;
import com.ferra13671.cometrenderer.glsl.uniform.GLUniform;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;

/**
 * Расширение класса униформы, которое реализовывает возможность записи параметра в униформу только 1 типа.
 *
 * @param <T> тип объекта, который можно будет записать в униформу (может быть никак не связан с типом униформы).
 *
 * @see GLUniform
 * @see UniformType
 */
public abstract class OneTypeGLUniform<T> extends GLUniform {
    /** Объект, который будет записан как параметр в униформу. **/
    protected T value = null;

    /**
     * @param name имя униформы.
     * @param location локация униформы в OpenGL.
     * @param glProgram программа ({@link GLProgram}), к которой привязана униформа.
     */
    public OneTypeGLUniform(String name, int location, GLProgram glProgram) {
        super(name, location, glProgram);
    }

    /**
     * Устанавливает объект в униформу.
     *
     * @param value объект, который будет записан как параметр в униформу.
     */
    public void set(T value) {
        this.value = value;
        this.program.addUpdatedUniform(this);
    }
}
