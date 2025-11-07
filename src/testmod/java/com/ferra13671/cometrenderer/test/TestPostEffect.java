package com.ferra13671.cometrenderer.test;

import com.ferra13671.cometrenderer.CometLoaders;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.GlslFileEntry;
import com.ferra13671.cometrenderer.buffer.framebuffer.CometFrameBuffer;
import com.ferra13671.cometrenderer.posteffect.PostEffectPipeline;
import com.ferra13671.cometrenderer.posteffect.ProgramPass;
import com.ferra13671.cometrenderer.posteffect.minecraft.OverrideFrameBufferVertexConsumerProvider;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import org.joml.Vector2f;

import java.awt.*;

public class TestPostEffect implements Mc {
    public static final CometFrameBuffer handsFrameBuffer = new CometFrameBuffer("Hands mesh", 1, 1, new Color(0f, 0f, 0f, 0f).hashCode(), false);


    public static final GlslFileEntry postEffectFragmentEntry = CometLoaders.IN_JAR.createShaderEntry("post-effect-fragment", "post-effect-test.fsh");
    public static GlProgram postEffectProgram = CometLoaders.IN_JAR.createProgramBuilder(CometRenderer.getMatrixSnippet())
            .name("post-effect-program")
            .vertexShader(TestMod.positionVertexEntry)
            .fragmentShader(postEffectFragmentEntry)
            .sampler("u_Texture")
            .uniform("texelSize", UniformType.VEC2)
            .build();
    public static PostEffectPipeline postEffect = PostEffectPipeline.builder()
            .pass(
                    ProgramPass.builder()
                            .program(postEffectProgram)
                            .output(mc.getFramebuffer())
                            .input(0, handsFrameBuffer)
                            .preRenderAction(program -> {
                                program.getUniform("texelSize", UniformType.VEC2).set(new Vector2f(1f / handsFrameBuffer.textureWidth, 1f / handsFrameBuffer.textureHeight));
                            })
                            .build()
            )
            .build();

    public static final OverrideFrameBufferVertexConsumerProvider handsVertexConsumer = new OverrideFrameBufferVertexConsumerProvider(
            mc.getBufferBuilders().getEntityVertexConsumers(),
            handsFrameBuffer,
            mc.getFramebuffer(),
            true,
            true
    );
}
