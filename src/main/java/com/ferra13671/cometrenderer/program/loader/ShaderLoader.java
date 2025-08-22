package com.ferra13671.cometrenderer.program.loader;

import com.ferra13671.cometrenderer.exceptions.CompileShaderException;
import com.ferra13671.cometrenderer.program.shader.Shader;
import com.ferra13671.cometrenderer.program.shader.ShaderType;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.InputStreamReader;

/*
 * Загрузчик шейдера
 */
public class ShaderLoader {

    /*
     * Загружает шейдер указанного типа с зашифрованного файла
     */
    public static Shader loadShader(String name, String id, ShaderType shaderType) {
        InputStream inputStream = ShaderLoader.class.getClassLoader().getResourceAsStream(id);
        String content;
        try {
            content = IOUtils.toString(new InputStreamReader(inputStream));
            inputStream.close();
        } catch (Exception e) {
            throw new CompileShaderException(e);
        }
        return Shader.compile(name, content, shaderType);
    }
}
