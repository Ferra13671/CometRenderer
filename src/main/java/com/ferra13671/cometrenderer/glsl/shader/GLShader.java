package com.ferra13671.cometrenderer.glsl.shader;

import com.ferra13671.cometrenderer.glsl.GLSLLoader;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.glsl.compiler.CometCompiler;
import com.ferra13671.cometrenderer.glsl.GLProgram;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import org.apiguardian.api.API;

import java.io.Closeable;

/**
 * Часть программы, исполняемая на GPU, которая отвечает за часть графической обработки программой: обработка вершин, растеризация, вычисление цвета пикселя и другие этапы рендеринга.
 *
 * @see <a href="https://wikis.khronos.org/opengl/Shader">OpenGL shader wiki</a>
 * @see GLProgram
 * @see ShaderType
 * @see CometCompiler
 */
@API(status = API.Status.MAINTAINED, since = "2.7")
public record GLShader(String name, int id, ShaderType shaderType, Registry registry) implements Closeable {

    @API(status = API.Status.MAINTAINED, since = "3.0")
    @Override
    public void close() {
        CometRenderer.getDevice().deleteShader(id());
    }

    @API(status = API.Status.MAINTAINED, since = "3.0")
    public static <T> GLShaderBuilder<T> builder(GLSLLoader<T> loader) {
        return new GLShaderBuilder<>(loader);
    }
}
