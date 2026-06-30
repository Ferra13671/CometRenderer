package com.ferra13671.cometrenderer.glsl.shader;

import com.ferra13671.cometrenderer.glsl.compiler.CometCompiler;
import com.ferra13671.cometrenderer.glsl.GLProgram;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import org.apiguardian.api.API;
import org.lwjgl.opengl.GL20;

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

    @Override
    @API(status = API.Status.INTERNAL)
    public void close() {
        GL20.glDeleteShader(this.id);
    }
}
