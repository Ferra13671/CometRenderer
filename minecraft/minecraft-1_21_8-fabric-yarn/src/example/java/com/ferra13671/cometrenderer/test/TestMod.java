package com.ferra13671.cometrenderer.test;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.plugins.minecraft.MinecraftPlugin;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import com.ferra13671.cometrenderer.test.mixins.IGlGpuBuffer;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import com.ferra13671.cometrenderer.CometVertexFormats;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;
import com.ferra13671.gltextureutils.ColorMode;
import com.ferra13671.gltextureutils.GLTexture;
import com.ferra13671.gltextureutils.TextureFiltering;
import com.ferra13671.gltextureutils.TextureWrapping;
import com.ferra13671.gltextureutils.builder.GLTextureInfo;
import com.ferra13671.gltextureutils.loader.TextureLoader;
import com.ferra13671.gltextureutils.loader.TextureLoaders;
import org.joml.Vector4f;

import java.util.Random;

public class TestMod implements Mc {

    private static final TextureLoader<String> textureLoader = new TextureLoader<>() {
        @Override
        public GLTextureInfo load(String s, ColorMode colorMode) throws Exception {
            return TextureLoaders.INPUT_STREAM.load(TestMod.class.getClassLoader().getResourceAsStream(s), colorMode);
        }
    };
    private static GLTexture texture;

    public static Mesh standaloneMesh;

    public static void initRender() {
        CometRenderer.init();
        MinecraftPlugin.init(glGpuBuffer -> ((IGlGpuBuffer) glGpuBuffer)._getId(), () -> mc.getWindow().getScaleFactor());

        standaloneMesh = CometRenderer.createMesh(DrawMode.QUADS, CometVertexFormats.POSITION_TEXTURE_COLOR, buffer -> {
            buffer.vertex(400, 250, 0)
                    .element("Texture", VertexElementType.FLOAT, 0f, 0f)
                    .element("Color", VertexElementType.FLOAT, 1f, 1f, 1f, 1f);
            buffer.vertex(400, 300, 0)
                    .element("Texture", VertexElementType.FLOAT, 0f, 1f)
                    .element("Color", VertexElementType.FLOAT, 1f, 1f, 1f, 1f);
            buffer.vertex(450, 300, 0)
                    .element("Texture", VertexElementType.FLOAT, 1f, 1f)
                    .element("Color", VertexElementType.FLOAT, 1f, 1f, 1f, 1f);
            buffer.vertex(450, 250, 0)
                    .element("Texture", VertexElementType.FLOAT, 1f, 0f)
                    .element("Color", VertexElementType.FLOAT, 1f, 1f, 1f, 1f);
        }).makeStandalone();

        texture =
                textureLoader.createTextureBuilder()
                        .name("Test-texture")
                        .info("texture.jpg")
                        .filtering(TextureFiltering.SMOOTH)
                        .wrapping(TextureWrapping.DEFAULT)
                        .build();
    }

    public static void render() {
        if (mc.player == null)
            return;

        MinecraftPlugin.getInstance().bindMainFramebuffer(true);

        drawRandomColorRect();

        drawMultiColorRect();

        drawTextureRect();
    }

    private static void drawRandomColorRect() {
        CometRenderer.setGlobalProgram(MinecraftPlugin.getInstance().getPrograms().POSITION);
        Random random = new Random();
        CometRenderer.setShaderColor(new Vector4f(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1f));
        MinecraftPlugin.getInstance().initMatrix();
        CometRenderer.initShaderColor();

        CometRenderer.draw(CometRenderer.createMesh(DrawMode.QUADS, CometVertexFormats.POSITION, buffer -> {
            buffer.vertex(200, 200, 0);
            buffer.vertex(200, 400, 0);
            buffer.vertex(400, 400, 0);
            buffer.vertex(400, 200, 0);
        }));
    }

    private static void drawMultiColorRect() {
        CometRenderer.setGlobalProgram(MinecraftPlugin.getInstance().getPrograms().POSITION_COLOR);
        CometRenderer.resetShaderColor();
        MinecraftPlugin.getInstance().initMatrix();
        CometRenderer.initShaderColor();

        CometRenderer.draw(CometRenderer.createMesh(DrawMode.QUADS, CometVertexFormats.POSITION_COLOR, buffer -> {
            buffer.vertex(400, 200, 0).element("Color", VertexElementType.FLOAT, 1f, 1f, 1f, 1f);
            buffer.vertex(400, 250, 0).element("Color", VertexElementType.FLOAT, 1f, 0f, 0f, 1f);
            buffer.vertex(450, 250, 0).element("Color", VertexElementType.FLOAT, 0f, 1f, 0f, 1f);
            buffer.vertex(450, 200, 0).element("Color", VertexElementType.FLOAT, 0f, 0f, 1f, 1f);
        }));
    }

    private static void drawTextureRect() {
        CometRenderer.resetShaderColor();
        CometRenderer.setGlobalProgram(MinecraftPlugin.getInstance().getPrograms().POSITION_TEXTURE_COLOR);
        MinecraftPlugin.getInstance().initMatrix();
        CometRenderer.initShaderColor();
        CometRenderer.getGlobalProgram().getUniform("u_Texture", UniformType.SAMPLER).set(texture);

        CometRenderer.draw(standaloneMesh, false);
        CometRenderer.resetShaderColor();
    }
}
