package _3d;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.cometrenderer.buffer.framebuffer.FramebufferImpl;
import com.ferra13671.cometrenderer.buffer.framebuffer.FramebufferInfo;
import com.ferra13671.cometrenderer.glsl.GLProgram;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import com.ferra13671.cometrenderer.plugins.glfw.CometGLFW;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import lombok.experimental.UtilityClass;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@UtilityClass
public class World {
    private final Matrix4f cubeRotateMatrix = new Matrix4f();

    private final VertexFormat POSITION_COLOR_TEXTURE_NORMAL = VertexFormat.POSITION_COLOR_TEXTURE.toBuilder()
            .element("Normal", VertexElementType.VECTOR_3_FLOAT, 1)
            .build();

    public Framebuffer shadowFramebuffer = new FramebufferImpl(FramebufferInfo.builder()
            .name("Shadow Map")
            .width(4096)
            .height(4096)
            .useStencil(false)
            .build()
    );

    public final float ambientLight = 0.2f;
    public final Vector3f sunVector = new Vector3f(-0.5f, -1f, -0.5f);

    private final Vector3f[] cubeVertices = new Vector3f[]{
            new Vector3f(1f, 1f, 1f),
            new Vector3f(1f, 1f, -1f),
            new Vector3f(-1f, 1f, -1f),
            new Vector3f(-1f, 1f, 1f),

            new Vector3f(1f, 1f, -1f),
            new Vector3f(1f, -1f, -1f),
            new Vector3f(-1f, -1f, -1f),
            new Vector3f(-1f, 1f, -1f),

            new Vector3f(-1f, 1f, -1f),
            new Vector3f(-1f, -1f, -1f),
            new Vector3f(-1f, -1f, 1f),
            new Vector3f(-1f, 1f, 1f),

            new Vector3f(-1f, 1f, 1f),
            new Vector3f(-1f, -1f, 1f),
            new Vector3f(1f, -1f, 1f),
            new Vector3f(1f, 1f, 1f),

            new Vector3f(1f, 1f, 1f),
            new Vector3f(1f, -1f, 1f),
            new Vector3f(1f, -1f, -1f),
            new Vector3f(1f, 1f, -1f),

            new Vector3f(-1f, -1f, 1f),
            new Vector3f(-1f, -1f, -1f),
            new Vector3f(1f, -1f, -1f),
            new Vector3f(1f, -1f, 1f),
    };
    private final Vector3f[] cubeNormals = new Vector3f[]{
            new Vector3f(0.1f, 1f, 0.1f),
            new Vector3f(0.1f, 1f, -0.1f),
            new Vector3f(-0.1f, 1f, -0.1f),
            new Vector3f(-0.1f, 1f, 0.1f),
            new Vector3f(0.1f, 0.1f, -1f),
            new Vector3f(0.1f, -0.1f, -1f),
            new Vector3f(-0.1f, -0.1f, -1f),
            new Vector3f(-0.1f, 0.1f, -1f),
            new Vector3f(-1f, 0.1f, -0.1f),
            new Vector3f(-1f, -0.1f, -0.1f),
            new Vector3f(-1f, -0.1f, 0.1f),
            new Vector3f(-1f, 0.1f, 0.1f),
            new Vector3f(-0.1f, 0.1f, 1f),
            new Vector3f(-0.1f, -0.1f, 1f),
            new Vector3f(0.1f, -0.1f, 1f),
            new Vector3f(0.1f, 0.1f, 1f),
            new Vector3f(1f, 0.1f, 0.1f),
            new Vector3f(1f, -0.1f, 0.1f),
            new Vector3f(1f, -0.1f, -0.1f),
            new Vector3f(1f, 0.1f, 0.1f),
            new Vector3f(-0.1f, -1f, 0.1f),
            new Vector3f(-0.1f, -1f, -0.1f),
            new Vector3f(0.1f, -1f, -0.1f),
            new Vector3f(0.1f, -1f, 0.1f),
    };
    private final Vector2f[] quadUV = new Vector2f[] {
            new Vector2f(1, 0),
            new Vector2f(0, 0),
            new Vector2f(0, 1),
            new Vector2f(1, 1)
    };

    public void tick() {
        cubeRotateMatrix
                .rotate((float) Math.toRadians(Hello3D.tickTimer.getTickDelay() / 10f), 0f, 0f, 1f)
                .rotate((float) Math.toRadians(Hello3D.tickTimer.getTickDelay() / 15f), 0f, 1f, 0f);
    }

    public void render() {
        shadowFramebuffer.clearAll();
        CometRenderer.getDevice().getPipelineStateManager().setDepthTest(true);

        Matrix4f projectionMatrix = Hello3D.getProjectionMatrix(
                CometGLFW.getWindow().getFramebufferWidth(),
                CometGLFW.getWindow().getFramebufferHeight()
        );
        Matrix4f orthoMatrix = Hello3D.getOrthoMatrix(30);
        Matrix4f cameraViewMatrix = Hello3D.camera.getViewMatrix();
        Matrix4f sunViewMatrix = new Matrix4f().setLookAt(
                new Vector3f(Hello3D.camera.getPosition()).add(new Vector3f(sunVector).negate().mul(100)),
                new Vector3f(Hello3D.camera.getPosition()),
                new Vector3f(0f, 1f, 0f)
        );
        Matrix4f lightSpaceMatrix = new Matrix4f(orthoMatrix).mul(new Matrix4f(sunViewMatrix));

        shadowFramebuffer.bind(true);
        drawFloor(Hello3D.defaultMaterialShadowProgram, orthoMatrix, sunViewMatrix, lightSpaceMatrix);
        drawStoneCube(Hello3D.defaultMaterialShadowProgram, -3, 1, -3, cubeRotateMatrix, orthoMatrix, sunViewMatrix, lightSpaceMatrix);
        drawStoneCube(Hello3D.defaultMaterialShadowProgram, -3.6f, 0.3f, -3.7f, new Matrix4f(), orthoMatrix, sunViewMatrix, lightSpaceMatrix);

        CometGLFW.getWindow().getFramebuffer().bind(true);
        GL11.glEnable(GL11.GL_CULL_FACE);

        drawSun(projectionMatrix, cameraViewMatrix);
        drawFloor(Hello3D.defaultMaterialProgram, projectionMatrix, cameraViewMatrix, lightSpaceMatrix);
        drawStoneCube(Hello3D.defaultMaterialProgram, -3, 1, -3, cubeRotateMatrix, projectionMatrix, cameraViewMatrix, lightSpaceMatrix);
        drawStoneCube(Hello3D.defaultMaterialProgram, -3.6f, 0.3f, -3.7f, new Matrix4f(), projectionMatrix, cameraViewMatrix, lightSpaceMatrix);
    }

    void drawSun(Matrix4f projectionMatrix, Matrix4f viewMatrix) {
        Vector3f sunPosition = new Vector3f(sunVector).negate().mul(100);

        CometRenderer.setCurrentProgram(Hello3D.positionProgram);
        CometRenderer.getShaderColor().resetColor();
        CometRenderer.applyShaderColorUniform();
        CometRenderer.getCurrentProgram().getUniform("projection", UniformType.MATRIX4).set(projectionMatrix);
        CometRenderer.getCurrentProgram().getUniform("view", UniformType.MATRIX4).set(viewMatrix);

        CometRenderer.draw(CometRenderer.createMesh(DrawMode.QUADS, VertexFormat.POSITION, builder -> {
            for (Vector3f cubeVertex : cubeVertices) {
                Vector3f pos = new Vector3f(sunPosition).add(new Vector3f(cubeVertex).mul(5f));

                builder
                        .vertex(pos.x(), pos.y(), pos.z());
            }
        }));
    }

    void drawFloor(GLProgram program, Matrix4f projectionMatrix, Matrix4f viewMatrix, Matrix4f lightSpaceMatrix) {
        CometRenderer.setCurrentProgram(program);
        CometRenderer.applyShaderColorUniform();
        program.consumeIfUniformPresent("u_Texture", UniformType.SAMPLER, u -> u.setTexture(Hello3D.woodTexture));
        program.consumeIfUniformPresent("projection", UniformType.MATRIX4, u -> u.set(projectionMatrix));
        program.consumeIfUniformPresent("view", UniformType.MATRIX4, u -> u.set(viewMatrix));
        program.consumeIfUniformPresent("ambientLight", UniformType.FLOAT, u -> u.set(ambientLight));
        program.consumeIfUniformPresent("sunVector", UniformType.VEC3, u -> u.set(sunVector));
        program.consumeIfUniformPresent("lightSpaceMatrix", UniformType.MATRIX4, u -> u.set(lightSpaceMatrix));
        program.consumeIfUniformPresent("shadowMap", UniformType.SAMPLER, u -> u.setTexture(shadowFramebuffer.getDepthAndStencilTextureId()));

        CometRenderer.draw(CometRenderer.createMesh(DrawMode.QUADS, POSITION_COLOR_TEXTURE_NORMAL, builder ->
                builder
                        .vertex(100, 0, -100)
                        .element("Color", VertexElementType.FLOAT, 1f, 1f, 1f, 1f)
                        .element("Texture", VertexElementType.FLOAT, 100f, 0f)
                        .element("Normal", VertexElementType.FLOAT, 0f, 1f, 0f)
                        .vertex(-100, 0, -100)
                        .element("Color", VertexElementType.FLOAT, 1f, 1f, 1f, 1f)
                        .element("Texture", VertexElementType.FLOAT, 0f, 0f)
                        .element("Normal", VertexElementType.FLOAT, 0f, 1f, 0f)
                        .vertex(-100, 0, 100)
                        .element("Color", VertexElementType.FLOAT, 1f, 1f, 1f, 1f)
                        .element("Texture", VertexElementType.FLOAT, 0f, 100f)
                        .element("Normal", VertexElementType.FLOAT, 0f, 1f, 0f)
                        .vertex(100, 0, 100)
                        .element("Color", VertexElementType.FLOAT, 1f, 1f, 1f, 1f)
                        .element("Texture", VertexElementType.FLOAT, 100f, 100f)
                        .element("Normal", VertexElementType.FLOAT, 0f, 1f, 0f)
        ));
    }

    void drawStoneCube(GLProgram program, float x, float y, float z, Matrix4f matrix4f, Matrix4f projectionMatrix, Matrix4f viewMatrix, Matrix4f lightSpaceMatrix) {
        CometRenderer.setCurrentProgram(program);
        CometRenderer.applyShaderColorUniform();
        program.consumeIfUniformPresent("projection", UniformType.MATRIX4, u -> u.set(projectionMatrix));
        program.consumeIfUniformPresent("view", UniformType.MATRIX4, u -> u.set(viewMatrix));
        program.consumeIfUniformPresent("ambientLight", UniformType.FLOAT, u -> u.set(ambientLight));
        program.consumeIfUniformPresent("sunVector", UniformType.VEC3, u -> u.set(sunVector));
        program.consumeIfUniformPresent("lightSpaceMatrix", UniformType.MATRIX4, u -> u.set(lightSpaceMatrix));
        program.consumeIfUniformPresent("shadowMap", UniformType.SAMPLER, u -> u.setTexture(shadowFramebuffer.getDepthAndStencilTextureId()));
        program.consumeIfUniformPresent("u_Texture", UniformType.SAMPLER, u -> u.setTexture(Hello3D.stoneTexture));

        CometRenderer.draw(CometRenderer.createMesh(DrawMode.QUADS, POSITION_COLOR_TEXTURE_NORMAL, builder -> {
            for (int i = 0; i < cubeVertices.length; i ++) {
                Vector3f pos = new Vector3f(x, y, z).add(matrix4f.transformPosition(new Vector3f(cubeVertices[i]).mul(0.3f)));
                Vector3f normal = matrix4f.transformPosition(new Vector3f(cubeNormals[i]));

                builder
                        .vertex(pos.x(), pos.y(), pos.z())
                        .element("Color", VertexElementType.FLOAT, 1f, 1f, 1f, 1f)
                        .element("Texture", VertexElementType.VECTOR_2_FLOAT, quadUV[i % 4])
                        .element("Normal", VertexElementType.VECTOR_3_FLOAT, normal);
            }
        }));
    }
}
