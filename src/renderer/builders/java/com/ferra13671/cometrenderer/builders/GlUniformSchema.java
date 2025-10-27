package com.ferra13671.cometrenderer.builders;

import com.ferra13671.cometrenderer.program.uniform.GlUniform;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import org.lwjgl.opengl.GL20;

/*
 * Схема, используемая при создании униформы программы
 */
public record GlUniformSchema<T extends GlUniform>(String name, UniformType<T> uniformType) {

    /*
     * Возвращает айди юниформы в программе
     */
    public int getIdFromProgram(int programId) {
        return GL20.glGetUniformLocation(programId, name);
    }
}
