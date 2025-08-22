package com.ferra13671.cometrenderer.program.loader;

import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.schema.GlProgramSchema;
import com.ferra13671.cometrenderer.program.schema.ShaderSchema;
import com.ferra13671.cometrenderer.program.shader.Shader;

/*
 * Загрузчик программ
 */
public class GlProgramLoader {

    /*
     * Загружает программу при помощи её схемы
     */
    public static GlProgram loadProgram(GlProgramSchema programSchema) {
        return GlProgram.compile(programSchema.name(), loadShader(programSchema.vertexShader()), loadShader(programSchema.fragmentShader()), programSchema.uniforms());
    }

    /*
     * Загружает шейдер при помощи его схемы
     */
    private static Shader loadShader(ShaderSchema shaderSchema) {
        return ShaderLoader.loadShader(shaderSchema.name(), shaderSchema.shaderId(), shaderSchema.shaderType());
    }
}
