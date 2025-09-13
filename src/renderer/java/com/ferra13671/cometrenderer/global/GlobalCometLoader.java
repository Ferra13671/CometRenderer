package com.ferra13671.cometrenderer.global;

import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.builder.GlProgramSchema;
import com.ferra13671.cometrenderer.program.builder.ShaderSchema;
import com.ferra13671.cometrenderer.program.shader.GlShader;

/*
 * Загрузчик программ и шейдеров
 */
public class GlobalCometLoader {

    /*
     * Загружает программу при помощи её схемы
     */
    public static GlProgram loadProgram(GlProgramSchema programSchema) {
        return GlobalCometCompiler.compileProgram(
                programSchema.name(),
                loadShader(programSchema.vertexShader()),
                loadShader(programSchema.fragmentShader()),
                programSchema.uniforms()
        );
    }

    /*
     * Загружает шейдер при помощи его схемы
     */
    private static GlShader loadShader(ShaderSchema shaderSchema) {
        return GlobalCometCompiler.compileShader(
                shaderSchema.shaderEntry(),
                shaderSchema.shaderType()
        );
    }
}
