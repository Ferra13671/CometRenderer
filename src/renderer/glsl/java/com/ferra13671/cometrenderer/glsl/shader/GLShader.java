package com.ferra13671.cometrenderer.glsl.shader;

import com.ferra13671.cometrenderer.glsl.compiler.CometCompiler;
import com.ferra13671.cometrenderer.glsl.compiler.GLSLContent;
import com.ferra13671.cometrenderer.utils.Compilable;
import com.ferra13671.cometrenderer.glsl.GLProgram;
import com.ferra13671.cometrenderer.utils.compile.CompileResult;
import com.ferra13671.cometrenderer.utils.compile.CompileStatus;
import com.ferra13671.cometrenderer.utils.tag.Registry;
import lombok.Getter;
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
@Getter
@API(status = API.Status.MAINTAINED, since = "2.7")
public class GLShader implements Compilable, Closeable {
    private final String name;
    private final ShaderType shaderType;
    private final Registry registry;
    private int id;
    private GLSLContent content = null;
    private CompileResult compileResult = null;

    @API(status = API.Status.INTERNAL)
    public GLShader(String name, ShaderType shaderType, Registry registry) {
        this.name = name;
        this.shaderType = shaderType;
        this.registry = registry;

        this.id = GL20.glCreateShader(shaderType.glId);
    }

    @API(status = API.Status.INTERNAL)
    public void setContent(GLSLContent content) {
        this.content = content;
        this.compileResult = null;
    }

    @API(status = API.Status.INTERNAL)
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
    @API(status = API.Status.INTERNAL)
    public void close() {
        GL20.glDeleteShader(this.id);
        this.id = -1;
        this.compileResult = null;
    }
}
