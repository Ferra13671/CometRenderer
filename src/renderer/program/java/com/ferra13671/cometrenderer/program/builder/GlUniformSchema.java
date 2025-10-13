package com.ferra13671.cometrenderer.program.builder;

import com.ferra13671.cometrenderer.program.uniform.UniformType;
import org.lwjgl.opengl.GL20;

/*
 * Схема, используемая при создании униформы программы
 */
public record GlUniformSchema(String name, UniformType uniformType) {

    /*
     * Возвращает айди юниформы в программе
     */
    public int getIdFromProgram(int programId) {
        return GL20.glGetUniformLocation(programId, name);
    }
}
