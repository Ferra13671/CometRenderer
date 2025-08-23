package com.ferra13671.cometrenderer.test;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.schema.GlProgramBuilder;
import com.ferra13671.cometrenderer.program.schema.snippet.GlProgramSnippet;
import com.ferra13671.cometrenderer.program.schema.snippet.GlProgramSnippetBuilder;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import com.ferra13671.cometrenderer.program.uniform.uniforms.Vec4GlUniform;
import com.ferra13671.cometrenderer.test.mixins.IGlGpuBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import org.joml.Vector4f;
import org.slf4j.LoggerFactory;

public class TestMod implements ModInitializer, Mc {

    public static final GlProgramSnippet MATRIX_SNIPPET = GlProgramSnippetBuilder.builder()
            .uniform("Projection", UniformType.BUFFER)
            .uniform("modelViewMat", UniformType.MATRIX)
            .build();
    public static final GlProgramSnippet COLOR_SNIPPET = GlProgramSnippetBuilder.builder()
            .uniform("color", UniformType.VEC4)
            .build();

    private static GlProgram program;

    @Override
    public void onInitialize() {
        LoggerFactory.getLogger(TestMod.class).info("Test");
        CometRenderer.init(glGpuBuffer -> ((IGlGpuBuffer) glGpuBuffer)._getId());
    }

    public static void render() {
        if (program == null)
            program = GlProgramBuilder.builder(MATRIX_SNIPPET, COLOR_SNIPPET)
                    .name("test")
                    .vertexShader("test-vertex", "position.vsh")
                    .fragmentShader("test-fragment", "position.fsh")
                    .build();

        CometRenderer.bindMainFrameBuffer();

        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);

        buffer.vertex(200, 200, 0);
        buffer.vertex(200, 400, 0);
        buffer.vertex(400, 400, 0);
        buffer.vertex(400, 200, 0);

        RenderUtils.setMatrixUniforms(program);
        program.getUniform("color", Vec4GlUniform.class).set(new Vector4f(1f, 1f, 0f, 1f));

        program.bind();
        CometRenderer.drawBuffer(buffer.end());
        program.unBind();
    }
}
