package com.ferra13671.cometrenderer.global;

import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.builders.GlProgramSchema;
import com.ferra13671.cometrenderer.builders.ShaderSchema;
import com.ferra13671.cometrenderer.program.shader.GlShader;

/**
 * Глобальный загрузчик программ и шейдеров CometRender'а.
 *
 * @deprecated нужно убрать, т.к. более разумно будет использовать сразу {@link GlobalCometCompiler}
 *
 * @see GlobalCometCompiler
 */
//TODO убрать
@Deprecated(forRemoval = true)
public class GlobalCometLoader {

    /**
     * Загружает программу при помощи её схемы.
     *
     * @param programSchema схема программы.
     * @return программа, загруженная по схеме
     */
    public static GlProgram loadProgram(GlProgramSchema programSchema) {
        return GlobalCometCompiler.compileProgram(
                programSchema.name(),
                loadShader(programSchema.vertexShader()),
                loadShader(programSchema.fragmentShader()),
                programSchema.uniforms()
        );
    }

    /**
     * Загружает шейдер при помощи его схемы.
     *
     * @param shaderSchema схема шейдера.
     * @return шейдер, загруженный по схеме.
     */
    private static GlShader loadShader(ShaderSchema shaderSchema) {
        return GlobalCometCompiler.compileShader(
                shaderSchema.shaderEntry(),
                shaderSchema.shaderType()
        );
    }
}
