package com.ferra13671.cometrenderer.minecraft.blur;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.cometrenderer.buffer.framebuffer.FramebufferImpl;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import com.ferra13671.cometrenderer.minecraft.CRM;
import com.ferra13671.cometrenderer.minecraft.FramebufferUtils;
import com.ferra13671.cometrenderer.vertex.DrawMode;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector2f;

import java.awt.*;

public class BlurProvider {
    @Getter
    @Setter
    private BlurConfig blurConfig;

    @Getter
    private final FramebufferImpl blurFrameBuffer = new FramebufferImpl("Blur framebuffer", false, 1, 1, Color.blue, 1);
    private final FramebufferImpl blurFrameBufferInternal1 = new FramebufferImpl("Blur framebuffer internal 1", false, 1, 1, Color.blue, 1);
    private final FramebufferImpl blurFrameBufferInternal2 = new FramebufferImpl("Blur framebuffer internal 2", false, 1, 1, Color.blue, 1);

    public BlurProvider(BlurConfig blurConfig) {
        this.blurConfig = blurConfig;
    }

    public void blurFrame() {
        Framebuffer input = CRM.getMainFramebuffer();
        Framebuffer output = this.blurFrameBufferInternal1;

        FramebufferUtils.resizeToParent(this.blurFrameBuffer, input);
        FramebufferUtils.resizeToParent(this.blurFrameBufferInternal1, input);
        FramebufferUtils.resizeToParent(this.blurFrameBufferInternal2, input);

        float width = input.getWidth();
        float height = input.getHeight();

        Mesh mesh = Mesh.builder(DrawMode.QUADS, VertexFormat.POSITION)
                .vertex(0, 0, 0)
                .vertex(0, height, 0)
                .vertex(width, height, 0)
                .vertex(width, 0, 0)
                .buildNullable();

        CometRenderer.setCurrentProgram(CRM.getPrograms().BLUR_FRAME);
        CometRenderer.applyShaderColorUniform();
        CRM.applyMatrixUniform();
        CometRenderer.getCurrentProgram().getUniform("texelSize", UniformType.VEC2).set(new Vector2f(1f / width, 1f / height));

        for (int i = 0; i < this.blurConfig.passes.length; i++) {
            if (i == this.blurConfig.passes.length - 1)
                output = this.blurFrameBuffer;

            this.blurConfig.passes[i].draw(input, output, mesh);

            input = output;
            output = i % 2 == 0 ? this.blurFrameBufferInternal2 : this.blurFrameBufferInternal1;
        }

        CRM.getMainFramebuffer().bind(false);
        mesh.close();
    }
}
