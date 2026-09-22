package _3d;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.glsl.GLProgram;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import com.ferra13671.cometrenderer.plugins.glfw.*;
import com.ferra13671.cometrenderer.texture.GLTexture;
import com.ferra13671.cometrenderer.texture.PathMode;
import com.ferra13671.cometrenderer.texture.loader.FileEntry;
import com.ferra13671.cometrenderer.texture.loader.TextureLoader;
import lombok.experimental.UtilityClass;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

@UtilityClass
public class Hello3D {
    public GLTexture woodTexture;
    public GLTexture stoneTexture;

    public GLProgram positionProgram;
    public GLProgram defaultMaterialProgram;
    public GLProgram defaultMaterialShadowProgram;

    public final float fov = 90;

    public final Camera camera = new Camera();
    public final MouseListener mouseListener = new MouseListener();
    public final TickTimer tickTimer = new TickTimer();

    public long lastRenderTime = System.currentTimeMillis();

    public void main(String[] args) {
        CometGLFW.init(
                WindowHints.builder().build(),
                FullScreenSwitchCombination.F11,
                true
        );
        CometRenderer.init();

        createTextures();
        createPrograms();

        Window window = CometGLFW.getWindow();
        window.setTitle("Hello 3D");
        window.setVisible(true);
        window.setCursorGrabbed(true);
        window.setSize(1200, 700);
        window.setMonitorCenterPosition();

        CometGLFW.setVSync(VsyncMode.Adaptive);

        camera.setPosition(new Vector3f(0f, 2f, 0f));

        window.setMousePosCallback(mouseListener);
        window.setFramebufferSizeCallback((width, height) -> mouseListener.reset());
        window.setLoopCallback(() -> {
            if (tickTimer.shouldTick()) {
                World.tick();
                tickMovement(window);
                tickTimer.reset();
            }

            World.render();

            lastRenderTime = System.currentTimeMillis();
        });

        window.loop();
    }

    void tickMovement(Window window) {
        float xDelta = 0, yDelta = 0, zDelta = 0;

        xDelta += window.isPressedKey(GLFW.GLFW_KEY_W) ? 1 : 0;
        xDelta -= window.isPressedKey(GLFW.GLFW_KEY_S) ? 1 : 0;
        yDelta += window.isPressedKey(GLFW.GLFW_KEY_SPACE) ? 1 : 0;
        yDelta -= (window.isPressedKey(GLFW.GLFW_KEY_LEFT_SHIFT) || window.isPressedKey(GLFW.GLFW_KEY_RIGHT_SHIFT)) ? 1 : 0;
        zDelta += window.isPressedKey(GLFW.GLFW_KEY_D) ? 1 : 0;
        zDelta -= window.isPressedKey(GLFW.GLFW_KEY_A) ? 1 : 0;

        if (xDelta != 0 || yDelta != 0 || zDelta != 0) {
            //какашка
            Vector3f delta = camera.getLookVector(camera.getRotation().x(), 0).mul(xDelta).add(
                    camera.getLookVector(camera.getRotation().x() - 90f, 0).mul(zDelta)
            ).add(0, yDelta, 0).mul(1f / tickTimer.getTickDelay());
            camera.move(delta);
        }
    }

    public Matrix4f getProjectionMatrix(int width, int height) {
        return new Matrix4f().identity().setPerspective(
                (float) Math.toRadians(fov),
                (float) width / height,
                0.1f,
                1000.0f
        );
    }

    public Matrix4f getOrthoMatrix(int size) {
        return new Matrix4f().identity().setOrtho(-size, size, -size, size, 0.1f, 250);
    }

    void createTextures() {
        woodTexture = TextureLoader.FILE_ENTRY.createTextureBuilder()
                .name("wood")
                .info(new FileEntry("wood.png", PathMode.INSIDE_JAR))
                .build();
        stoneTexture = TextureLoader.FILE_ENTRY.createTextureBuilder()
                .name("stone")
                .info(new FileEntry("stone.png", PathMode.INSIDE_JAR))
                .build();
    }

    void createPrograms() {
        Shaders shaders = new Shaders();

        positionProgram = GLProgram.builder(
                CometRenderer.getColorSnippet()
        )
                .name("position")
                .shader(shaders.positionVertex)
                .shader(shaders.positionFragment)
                .build();

        defaultMaterialProgram = GLProgram.builder(
                CometRenderer.getColorSnippet()
        )
                .name("default_material")
                .shader(shaders.defaultMaterialVertex)
                .shader(shaders.defaultMaterialFragment)
                .uniform("lightSpaceMatrix", UniformType.MATRIX4)
                .build();

        defaultMaterialShadowProgram = GLProgram.builder()
                .name("default_material_shadow")
                .shader(shaders.defaultMaterialVertex)
                .shader(shaders.shadowTextureFragment)
                .build();
    }
}
