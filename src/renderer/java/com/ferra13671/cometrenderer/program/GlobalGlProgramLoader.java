package com.ferra13671.cometrenderer.program;

import com.ferra13671.cometrenderer.program.schema.GlProgramSchema;
import com.ferra13671.cometrenderer.program.schema.ShaderSchema;
import com.ferra13671.cometrenderer.program.shader.Shader;
import com.ferra13671.cometrenderer.program.shader.ShaderType;

import java.util.function.Function;

/*
 * Загрузчик программ
 */
public class GlobalGlProgramLoader {

    /*
     * Загружает программу при помощи её схемы
     */
    public static GlProgram loadProgram(GlProgramSchema programSchema) {
        return GlProgram.compile(programSchema.name(), loadShader(programSchema.vertexShader()), loadShader(programSchema.fragmentShader()), programSchema.uniforms());
    }

    /*
     * Загружает шейдер при помощи его схемы
     */
    private static <T> Shader loadShader(ShaderSchema<T> shaderSchema) {
        return loadShader(shaderSchema.name(), shaderSchema.contentGetter(), shaderSchema.shaderPath(), shaderSchema.shaderType());
    }

    /*
     * Загружает шейдер указанного типа
     */
    private static <T> Shader loadShader(String name, Function<T, String> contentGetter, T shaderPath, ShaderType shaderType) {
        return Shader.compile(name, contentGetter.apply(shaderPath), shaderType);
    }
}
