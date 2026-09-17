import com.ferra13671.cometrenderer.CometLoaders;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.glsl.GLProgram;
import com.ferra13671.cometrenderer.glsl.GLProgramBuilder;
import com.ferra13671.cometrenderer.glsl.shader.GLShader;
import com.ferra13671.cometrenderer.glsl.shader.ShaderType;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import com.ferra13671.cometrenderer.plugins.glfw.CometGLFW;
import com.ferra13671.cometrenderer.plugins.glfw.FullScreenSwitchCombination;
import com.ferra13671.cometrenderer.plugins.glfw.Window;
import com.ferra13671.cometrenderer.plugins.glfw.WindowHints;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import com.ferra13671.cometrenderer.vertex.mesh.IMesh;
import lombok.experimental.UtilityClass;
import org.joml.Matrix4f;

import java.awt.*;

@UtilityClass
public class HelloTriangle {
    String vertexShaderSrc = """
            #version 330 core
            
            in vec4 position;
            in vec4 vertexColor_in;
            
            uniform mat4 matrix;
            
            out vec4 vertexColor;
            
            void main() {
                gl_Position = position * matrix;
                vertexColor = vertexColor_in;
            }
            """;
    String fragmentShaderSrc = """
            #version 330 core
            
            precision lowp float;
            
            in vec4 vertexColor;
            
            out vec4 fragColor;
            
            void main() {
                fragColor = vertexColor;
            }
            """;

    private GLProgram program;

    private long lastTime = System.currentTimeMillis();

    public void main(String[] args) {
        CometGLFW.init(
                WindowHints.builder().build(),
                FullScreenSwitchCombination.F11,
                true
        );
        CometRenderer.init();

        createProgram();

        Window window = CometGLFW.getWindow();
        window.setTitle("Hello triangle");
        window.setVisible(true);

        Matrix4f rotateMatrix = new Matrix4f();
        window.setLoopCallback(() -> {
            window.getFramebuffer().bind(true);

            CometRenderer.setCurrentProgram(program);

            CometRenderer.getCurrentProgram().getUniform("matrix", UniformType.MATRIX4).set(
                    rotateMatrix.rotate((float) Math.toRadians((System.currentTimeMillis() - lastTime) / 10f), 0f, 0f, 1f)
            );
            lastTime = System.currentTimeMillis();

            IMesh mesh = CometRenderer.createMesh(DrawMode.TRIANGLES, VertexFormat.POSITION_COLOR, builder ->
                    builder
                            .vertex(-0.5f, -0.5f, 0.0f).element("Color", VertexElementType.FLOAT, 1f, 0f, 0f, 1f)
                            .vertex(0.0f, 0.5f, 0.0f).element("Color", VertexElementType.FLOAT, 0f, 1f, 0f, 1f)
                            .vertex(0.5f, -0.5f, 0.0f).element("Color", VertexElementType.FLOAT, 0f, 0f, 1f, 1f)
            );
            CometRenderer.draw(mesh);
        });

        window.loop();
    }

    void createProgram() {
        GLShader vertexShader = CometLoaders.STRING.createShaderBuilder()
                .info(
                        "vertex-shader",
                        vertexShaderSrc,
                        ShaderType.Vertex
                )
                .uniform("matrix", UniformType.MATRIX4)
                .build();

        GLShader fragmentShader = CometLoaders.STRING.createShaderBuilder()
                .info(
                        "fragment-shader",
                        fragmentShaderSrc,
                        ShaderType.Fragment
                )
                .build();

        program = new GLProgramBuilder<>()
                .name("example-program")
                .shader(vertexShader)
                .shader(fragmentShader)
                .build();
    }
}
