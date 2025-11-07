package com.ferra13671.cometrenderer.program.uniform.uniforms;

import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.uniform.GlUniform;
import com.ferra13671.cometrenderer.program.uniform.UniformType;

/**
 * Расширение класса униформы, которое реализовывает возможность записи параметра в униформу только 1 типа.
 *
 * @param <T> тип объекта, который можно будет записать в униформу (может быть никак не связан с типом униформы).
 *
 * @see GlUniform
 * @see UniformType
 */
public abstract class OneTypeGlUniform<T> extends GlUniform {
    /** Объект, который будет записан как параметр в униформу. **/
    protected T value = null;

    /**
     * @param name имя униформы.
     * @param location локация униформы в OpenGL.
     * @param glProgram программа ({@link GlProgram}), к которой привязана униформа.
     */
    public OneTypeGlUniform(String name, int location, GlProgram glProgram) {
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
