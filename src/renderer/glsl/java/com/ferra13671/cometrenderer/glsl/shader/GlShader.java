package com.ferra13671.cometrenderer.glsl.shader;

import com.ferra13671.cometrenderer.glsl.compiler.GlslContent;
import com.ferra13671.cometrenderer.utils.Compilable;
import com.ferra13671.cometrenderer.glsl.compiler.GlobalCometCompiler;
import com.ferra13671.cometrenderer.glsl.GlProgram;
import com.ferra13671.cometrenderer.utils.compile.CompileResult;
import com.ferra13671.cometrenderer.utils.compile.CompileStatus;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import org.lwjgl.opengl.GL20;

import java.io.Closeable;
import java.util.Map;

/**
 * Часть программы, исполняемая на GPU, которая отвечает за часть графической обработки программой: обработка вершин, растеризация, вычисление цвета пикселя и другие этапы рендеринга.
 *
 * @param name Имя шейдера.
 * @param content Контент шейдера.
 * @param id Айди шейдера в OpenGL.
 * @param extraUniforms Униформы, добавленные шейдером при интеграции в него шейдерных библиотек.
 * @param shaderType Тип шейдера.
 * @see <a href="https://wikis.khronos.org/opengl/Shader">OpenGL shader wiki</a>
 * @see GlProgram
 * @see ShaderType
 * @see GlobalCometCompiler
 */
public record GlShader(String name, GlslContent content, int id, Map<String, UniformType<?>> extraUniforms, ShaderType shaderType) implements Compilable, Closeable {

    @Override
    public void close() {
        GL20.glDeleteShader(id());
        this.extraUniforms.clear();
    }

    @Override
    public CompileResult getCompileResult() {
        CompileStatus status = CompileStatus.fromStatusId(GL20.glGetShaderi(id(), GL20.GL_COMPILE_STATUS));
        return new CompileResult(
                status,
                status == CompileStatus.FAILURE ? GL20.glGetShaderInfoLog(id()).trim() : ""
        );
    }
}
