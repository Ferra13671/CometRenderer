package com.ferra13671.cometrenderer.test;

import com.ferra13671.cometrenderer.CometLoaders;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.compiler.GlslFileEntry;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.shader.ShaderType;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import com.ferra13671.cometrenderer.test.mixins.IGlGpuBuffer;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import com.ferra13671.cometrenderer.CometVertexFormats;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;
import com.ferra13671.ferraguard.annotations.OverriddenMethod;
import com.ferra13671.gltextureutils.ColorMode;
import com.ferra13671.gltextureutils.GLTexture;
import com.ferra13671.gltextureutils.TextureFiltering;
import com.ferra13671.gltextureutils.TextureWrapping;
import com.ferra13671.gltextureutils.builder.GLTextureInfo;
import com.ferra13671.gltextureutils.loader.TextureLoader;
import com.ferra13671.gltextureutils.loader.TextureLoaders;
import net.fabricmc.api.ModInitializer;
import org.joml.Vector4f;

import java.util.Random;

public class TestMod implements ModInitializer, Mc {

    private static final TextureLoader<String> textureLoader = new TextureLoader<>() {
        @Override
        @OverriddenMethod
        public GLTextureInfo load(String s, ColorMode colorMode) throws Exception {
            return TextureLoaders.INPUT_STREAM.load(TestMod.class.getClassLoader().getResourceAsStream(s), colorMode);
        }
    };

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

    public static Mesh standaloneMesh;

    private static boolean initialized = false;

    @Override
    public void onInitialize() {}

    public static void render() {
        if (!initialized) {
            CometRenderer.init(glGpuBuffer -> ((IGlGpuBuffer) glGpuBuffer)._getId(), () -> mc.getWindow().getScaleFactor());
            initialized = true;
        }

        if (standaloneMesh == null)
            standaloneMesh = CometRenderer.createMesh(DrawMode.QUADS, CometVertexFormats.POSITION_COLOR_TEXTURE, buffer -> {
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
            }).makeStandalone();

        if (positionProgram == null)
            positionProgram = CometLoaders.IN_JAR.createProgramBuilder(CometRenderer.getMatrixSnippet(), CometRenderer.getColorSnippet())
                    .name("test1")
                    .shader(positionVertexEntry, ShaderType.Vertex)
                    .shader(positionFragmentEntry, ShaderType.Fragment)
                    .build();
        if (positionColorProgram == null)
            positionColorProgram = CometLoaders.IN_JAR.createProgramBuilder(CometRenderer.getMatrixSnippet(), CometRenderer.getColorSnippet())
                    .name("test2")
                    .shader(positionColorVertexEntry, ShaderType.Vertex)
                    .shader(positionColorFragmentEntry, ShaderType.Fragment)
                    .build();
        if (positionColorTextureProgram == null)
            positionColorTextureProgram = CometLoaders.IN_JAR.createProgramBuilder(CometRenderer.getMatrixSnippet(), CometRenderer.getColorSnippet())
                    .name("test3")
                    .shader(positionColorTextureVertexEntry, ShaderType.Vertex)
                    .shader(positionColorTextureFragmentEntry, ShaderType.Fragment)
                    .sampler("u_Texture")
                    .build();
        if (texture == null)
            texture =
                    textureLoader.createTextureBuilder()
                    .name("Test-texture")
                    .info("texture.jpg")
                    .filtering(TextureFiltering.SMOOTH)
                    .wrapping(TextureWrapping.DEFAULT)
                    .build();

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

        //------ multi-colored textured rect with standalone mesh ------//
        CometRenderer.resetShaderColor();
        CometRenderer.setGlobalProgram(positionColorTextureProgram);
        CometRenderer.initMatrix();
        CometRenderer.initShaderColor();
        positionColorTextureProgram.getUniform("u_Texture", UniformType.SAMPLER).set(texture);

        CometRenderer.draw(standaloneMesh, false);
        CometRenderer.resetShaderColor();
    }
}
