package com.ferra13671.cometrenderer.test;

import com.ferra13671.TextureUtils.builder.GLTextureBuilder;
import com.ferra13671.TextureUtils.loader.TextureLoader;
import com.ferra13671.TextureUtils.loader.TextureLoaders;
import com.ferra13671.TextureUtils.texture.GLTexture;
import com.ferra13671.TextureUtils.texture.TextureFiltering;
import com.ferra13671.TextureUtils.texture.TextureWrapping;
import com.ferra13671.cometrenderer.CometLoaders;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.GlslFileEntry;
import com.ferra13671.cometrenderer.global.GlobalCometCompiler;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import com.ferra13671.cometrenderer.shaderlibrary.GlShaderLibraries;
import com.ferra13671.cometrenderer.test.mixins.IGlGpuBuffer;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import com.ferra13671.cometrenderer.CometVertexFormats;
import net.fabricmc.api.ModInitializer;
import org.joml.Vector4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.function.Function;

public class TestMod implements ModInitializer, Mc {
    private final Logger logger = LoggerFactory.getLogger(TestMod.class);

    private static final Function<String, TextureLoader> textureLoader = path -> TextureLoaders.INPUT_STREAM.apply(TestMod.class.getClassLoader().getResourceAsStream(path));

    private static GlProgram positionProgram;
    public static final GlslFileEntry positionVertexEntry = CometLoaders.IN_JAR.createGlslFileEntry("test-vertex", "position.vsh");
    private static final GlslFileEntry positionFragmentEntry = CometLoaders.IN_JAR.createGlslFileEntry("test-fragment", "position.fsh");
    private static GlProgram positionColorProgram;
    private static final GlslFileEntry positionColorVertexEntry = CometLoaders.IN_JAR.createGlslFileEntry("test-vertex2", "position-colored.vsh");
    private static final GlslFileEntry positionColorFragmentEntry = CometLoaders.IN_JAR.createGlslFileEntry("test-fragment2", "position-colored.fsh");
    private static GlProgram positionColorTextureProgram;
    private static final GlslFileEntry positionColorTextureVertexEntry = CometLoaders.IN_JAR.createGlslFileEntry("test-vertex3", "texture-colored.vsh");
    private static final GlslFileEntry positionColorTextureFragmentEntry = CometLoaders.IN_JAR.createGlslFileEntry("test-fragment3", "texture-colored.fsh");
    private static GLTexture texture;

    /*
    public static final VertexElementType<Color> COLOR = new VertexElementType<>(
            4 * 4,
            "Float",
            GL11.GL_FLOAT,
            Color.class,
            (pointer, data) -> {
                for (int i = 0; i < data.length; i++) {
                    long ptr = pointer + (16L * i);
                    MemoryUtil.memPutFloat(ptr, data[i].getRed() / 255f);
                    MemoryUtil.memPutFloat(ptr + 4L, data[i].getGreen() / 255f);
                    MemoryUtil.memPutFloat(ptr + 8L, data[i].getBlue() / 255f);
                    MemoryUtil.memPutFloat(ptr + 12L, data[i].getAlpha() / 255f);
                }
            }
    );
    public static final VertexFormat CUSTOM_FORMAT = VertexFormatBuilder.builder()
            .element("Color", COLOR, 1)
            .build();

     */

    private static int prevScale = 0;

    @Override
    public void onInitialize() {
        LoggerFactory.getLogger(TestMod.class).info("Test");
        CometRenderer.init(glGpuBuffer -> ((IGlGpuBuffer) glGpuBuffer)._getId(), () -> mc.getWindow().getScaleFactor());

        //------ LIBRARY TEST ------//
        logger.info("Test library system...");
        GlShaderLibraries.addLibraries(
                CometLoaders.IN_JAR.createLibraryBuilder()
                        .name("test")
                        .library("test.glsl")
                        .build(),
                CometLoaders.IN_JAR.createLibraryBuilder()
                        .name("test2")
                        .library("test2.glsl")
                        .build()
        );
        logger.info(GlobalCometCompiler.includeShaderLibraries("#include<test>").content());
        //--------------------------//
    }

    public static void render() {
        if (positionProgram == null)
            positionProgram = CometLoaders.IN_JAR.createProgramBuilder(CometRenderer.getMatrixSnippet(), CometRenderer.getColorSnippet())
                    .name("test1")
                    .vertexShader(positionVertexEntry)
                    .fragmentShader(positionFragmentEntry)
                    .build();
        if (positionColorProgram == null)
            positionColorProgram = CometLoaders.IN_JAR.createProgramBuilder(CometRenderer.getMatrixSnippet(), CometRenderer.getColorSnippet())
                    .name("test2")
                    .vertexShader(positionColorVertexEntry)
                    .fragmentShader(positionColorFragmentEntry)
                    .build();
        if (positionColorTextureProgram == null)
            positionColorTextureProgram = CometLoaders.IN_JAR.createProgramBuilder(CometRenderer.getMatrixSnippet(), CometRenderer.getColorSnippet())
                    .name("test3")
                    .vertexShader(positionColorTextureVertexEntry)
                    .fragmentShader(positionColorTextureFragmentEntry)
                    .sampler("u_Texture")
                    .build();
        if (texture == null)
            texture =
                    GLTextureBuilder.builder()
                    .name("Test-texture")
                    .loader(textureLoader.apply("texture.jpg"))
                    .filtering(TextureFiltering.SMOOTH)
                    .wrapping(TextureWrapping.DEFAULT)
                    .build();

        CometRenderer.bindMainFramebuffer();

        if (TestPostEffect.handsFrameBuffer.textureWidth != mc.getFramebuffer().textureWidth || TestPostEffect.handsFrameBuffer.textureHeight != mc.getFramebuffer().textureHeight || prevScale != mc.getWindow().getScaleFactor()) {

            TestPostEffect.handsFrameBuffer.resize(mc.getFramebuffer().textureWidth, mc.getFramebuffer().textureHeight);
            TestPostEffect.handsFrameBuffer.clearColorTexture();
            prevScale = mc.getWindow().getScaleFactor();
        }
        //mc.player.sendMessage(Text.literal(mc.getFramebuffer().textureWidth + " " + mc.getFramebuffer().textureHeight), false);

        TestPostEffect.postEffect.execute(mc.getWindow().getWidth(), mc.getWindow().getHeight());
        TestPostEffect.handsFrameBuffer.clearColorTexture();

        CometRenderer.bindMainFramebuffer();

        //------ Rect with random color with position program ------//
        CometRenderer.setGlobalProgram(positionProgram);
        Random random = new Random();
        CometRenderer.setShaderColor(new Vector4f(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1f));
        CometRenderer.initMatrix();
        CometRenderer.initShaderColor();

        CometRenderer.draw(CometRenderer.createMesh(DrawMode.QUADS, CometVertexFormats.POSITION, buffer -> {
            buffer.vertex(200, 200, 0);
            buffer.vertex(200, 400, 0);
            buffer.vertex(400, 400, 0);
            buffer.vertex(400, 200, 0);
        }));

        //------ multi-colored rect with position-color program ------//
        CometRenderer.setGlobalProgram(positionColorProgram);
        CometRenderer.resetShaderColor();
        CometRenderer.initMatrix();
        CometRenderer.initShaderColor();

        CometRenderer.draw(CometRenderer.createMesh(DrawMode.QUADS, CometVertexFormats.POSITION_COLOR, buffer -> {
            buffer.vertex(400, 200, 0).element("Color", VertexElementType.FLOAT, 1f, 1f, 1f, 1f);
            buffer.vertex(400, 250, 0).element("Color", VertexElementType.FLOAT, 1f, 0f, 0f, 1f);
            buffer.vertex(450, 250, 0).element("Color", VertexElementType.FLOAT, 0f, 1f, 0f, 1f);
            buffer.vertex(450, 200, 0).element("Color", VertexElementType.FLOAT, 0f, 0f, 1f, 1f);
        }));

        //------ multi-colored textured rect with position-color-texture program ------//
        CometRenderer.resetShaderColor();
        CometRenderer.setGlobalProgram(positionColorTextureProgram);
        CometRenderer.initMatrix();
        CometRenderer.initShaderColor();
        positionColorTextureProgram.getUniform("u_Texture", UniformType.SAMPLER).set(texture);

        CometRenderer.draw(CometRenderer.createMesh(DrawMode.QUADS, CometVertexFormats.POSITION_COLOR_TEXTURE, buffer -> {
            buffer.vertex(400, 250, 0)
                    .element("Color", VertexElementType.FLOAT, 1f, 1f, 1f, 1f)
                    .element("Texture", VertexElementType.FLOAT, 0f, 0f);
            buffer.vertex(400, 300, 0)
                    .element("Color", VertexElementType.FLOAT, 1f, 1f, 1f, 1f)
                    .element("Texture", VertexElementType.FLOAT, 0f, 1f);
            buffer.vertex(450, 300, 0)
                    .element("Color", VertexElementType.FLOAT, 1f, 1f, 1f, 1f)
                    .element("Texture", VertexElementType.FLOAT, 1f, 1f);
            buffer.vertex(450, 250, 0)
                    .element("Color", VertexElementType.FLOAT, 1f, 1f, 1f, 1f)
                    .element("Texture", VertexElementType.FLOAT, 1f, 0f);
        }));
        CometRenderer.resetShaderColor();
    }
}
