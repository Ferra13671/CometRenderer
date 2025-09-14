package com.ferra13671.cometrenderer.test;

import com.ferra13671.cometrenderer.CometLoaders;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.GlslFileEntry;
import com.ferra13671.cometrenderer.global.GlobalCometCompiler;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.shaderlibrary.GlShaderLibraries;
import com.ferra13671.cometrenderer.test.mixins.IGlGpuBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.render.VertexFormats;
import org.joml.Vector4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class TestMod implements ModInitializer, Mc {
    private final Logger logger = LoggerFactory.getLogger(TestMod.class);

    private static GlProgram program;
    private static final GlslFileEntry vertexEntry = CometLoaders.IN_JAR.createShaderEntry("test-vertex", "position.vsh");
    private static final GlslFileEntry fragmentEntry = CometLoaders.IN_JAR.createShaderEntry("test-fragment", "position.fsh");

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
        logger.info(GlobalCometCompiler.includeShaderLibraries("#include<test>"));
        //--------------------------//
    }

    public static void render() {
        if (program == null)
            program = CometLoaders.IN_JAR.createProgramBuilder(CometRenderer.getMatrixSnippet(), CometRenderer.getColorSnippet())
                    .name("test")
                    .vertexShader(vertexEntry)
                    .fragmentShader(fragmentEntry)
                    .build();

        CometRenderer.bindMainFrameBuffer();

        CometRenderer.setGlobalProgram(program);

        Random random = new Random();

        CometRenderer.setShaderColor(new Vector4f(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1f));
        CometRenderer.initMatrix();
        CometRenderer.initShaderColor();

        CometRenderer.drawBuffer(CometRenderer.createBuiltBuffer(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION, buffer -> {
            buffer.vertex(200, 200, 0);
            buffer.vertex(200, 400, 0);
            buffer.vertex(400, 400, 0);
            buffer.vertex(400, 200, 0);
        }));
    }
}
