package com.ferra13671.cometrenderer.builders;

import com.ferra13671.cometrenderer.program.uniform.GlUniform;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import org.lwjgl.opengl.GL20;

/**
 * Объект, представляющий собой схему униформу, используемую при создании униформы программы.
 *
 * @param name имя униформы программы.
 * @param uniformType тип униформы программы.
 * @param <T> униформа.
 */
public record GlUniformSchema<T extends GlUniform>(String name, UniformType<T> uniformType) {

    /**
     * Возвращает локацию униформы в программе.
     *
     * @param programId айди программы в OpenGL.
     * @return локация униформы в программе.
     */
    public int getLocationFromProgram(int programId) {
        return GL20.glGetUniformLocation(programId, name);
    }
}
