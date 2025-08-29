package com.ferra13671.cometrenderer.global;

import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.builder.GlProgramSchema;
import com.ferra13671.cometrenderer.program.builder.GlShaderLibrarySchema;
import com.ferra13671.cometrenderer.program.builder.ShaderSchema;
import com.ferra13671.cometrenderer.program.shader.GlShader;
import com.ferra13671.cometrenderer.program.shader.ShaderType;
import com.ferra13671.cometrenderer.shaderlibrary.GlShaderLibrary;

import java.util.function.Function;

/*
 * Загрузчик программ, шейдеров и библиотек
 */
public class GlobalCometLoader {

    /*
     * Загружает программу при помощи её схемы
     */
    public static <T> GlProgram loadProgram(GlProgramSchema<T> programSchema) {
        return GlobalCometCompiler.compileProgram(
                programSchema.name(),
                loadShader(programSchema.vertexShader()),
                loadShader(programSchema.fragmentShader()),
                programSchema.uniforms()
        );
    }

    /*
     * Загружает шейдерную библиотеку при помощи её схемы
     */
    public static <T> GlShaderLibrary loadLibrary(GlShaderLibrarySchema<T> librarySchema) {
        return new GlShaderLibrary(
                librarySchema.name(),
                librarySchema.contentGetter().apply(librarySchema.shaderPath()),
                librarySchema.uniforms()
        );
    }

    /*
     * Загружает шейдер при помощи его схемы
     */
    private static <T> GlShader loadShader(ShaderSchema<T> shaderSchema) {
        return loadShader(
                shaderSchema.name(),
                shaderSchema.contentGetter(),
                shaderSchema.shaderPath(),
                shaderSchema.shaderType()
        );
    }

    /*
     * Загружает шейдер указанного типа
     */
    private static <T> GlShader loadShader(String name, Function<T, String> contentGetter, T shaderPath, ShaderType shaderType) {
        return GlobalCometCompiler.compileShader(
                name,
                contentGetter.apply(shaderPath),
                shaderType
        );
    }
}
