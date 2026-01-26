package com.ferra13671.cometrenderer.glsl.shader;

import com.ferra13671.cometrenderer.glsl.compiler.GlslContent;
import com.ferra13671.cometrenderer.utils.Compilable;
import com.ferra13671.cometrenderer.glsl.compiler.GlobalCometCompiler;
import com.ferra13671.cometrenderer.glsl.GlProgram;
import com.ferra13671.cometrenderer.utils.compile.CompileResult;
import com.ferra13671.cometrenderer.utils.compile.CompileStatus;
import lombok.Getter;
import org.lwjgl.opengl.GL20;

import java.io.Closeable;

/**
 * Часть программы, исполняемая на GPU, которая отвечает за часть графической обработки программой: обработка вершин, растеризация, вычисление цвета пикселя и другие этапы рендеринга.
 *
 * @see <a href="https://wikis.khronos.org/opengl/Shader">OpenGL shader wiki</a>
 * @see GlProgram
 * @see ShaderType
 * @see GlobalCometCompiler
 */
public class GlShader implements Compilable, Closeable {
    @Getter
    private final String name;
    @Getter
    private final ShaderType shaderType;
    @Getter
    private int id;
    @Getter
    private GlslContent content = null;
    @Getter
    private CompileResult compileResult = null;

    public GlShader(String name, ShaderType shaderType) {
        this.name = name;
        this.shaderType = shaderType;
        this.id = GL20.glCreateShader(shaderType.glId);
    }

    public void setContent(GlslContent content) {
        this.content = content;
        this.compileResult = null;
    }

    public void compile() {
        if (this.compileResult == null) {
            GL20.glShaderSource(this.id, this.content.concatLines());
            GL20.glCompileShader(this.id);

            CompileStatus status = CompileStatus.fromStatusId(GL20.glGetShaderi(getId(), GL20.GL_COMPILE_STATUS));
            this.compileResult = new CompileResult(
                    status,
                    status == CompileStatus.FAILURE ? GL20.glGetShaderInfoLog(getId()).trim() : ""
            );
        }
    }

    @Override
    public void close() {
        GL20.glDeleteShader(this.id);
        this.id = -1;
        this.compileResult = null;
    }
}
