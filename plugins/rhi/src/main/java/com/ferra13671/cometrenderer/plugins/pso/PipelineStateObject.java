package com.ferra13671.cometrenderer.plugins.pso;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.device.GLDevice;
import com.ferra13671.cometrenderer.glsl.GLProgram;
import com.ferra13671.cometrenderer.utils.*;
import com.ferra13671.cometrenderer.utils.blend.DstFactor;
import com.ferra13671.cometrenderer.utils.blend.SrcFactor;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.apiguardian.api.API;

import java.io.Closeable;

@API(status = API.Status.MAINTAINED, since = "3.1")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PipelineStateObject implements Bindable, Closeable {
    @Getter
    final String name;
    final GLProgram program;
    @Builder.Default
    BlendFunc blend = null;
    final boolean depthTest;
    @Builder.Default
    boolean depthMask = true;
    @Builder.Default
    CompareFunction depthFunc = CompareFunction.LESS;
    @Builder.Default
    Face cullFace = Face.Back;
    @Builder.Default
    FrontFace frontFace = FrontFace.CounterClockwise;
    @Builder.Default
    @NonNull
    PolygonMode polygonMode = PolygonMode.Fill;

    @Override
    public void bind() {
        CometRenderer.setCurrentProgram(this.program);
        GLDevice device = CometRenderer.getDevice();

        if (this.blend == null)
            CometRenderer.disableBlend();
        else
            CometRenderer.setBlend(this.blend.srcColor, this.blend.dstColor, this.blend.srcAlpha, this.blend.dstAlpha);

        device.getPipelineStateManager().setDepthTest(this.depthTest);
        device.getPipelineStateManager().setDepthMask(this.depthMask);
        if (this.cullFace == null)
            CometRenderer.disableCullFace();
        else {
            CometRenderer.enableCullFace();
            CometRenderer.setCullFaceMode(this.cullFace);
        }

        CometRenderer.setPolygonMode(this.polygonMode);
    }

    @Override
    public void close() {}

    public record BlendFunc(SrcFactor srcColor, DstFactor dstColor, SrcFactor srcAlpha, DstFactor dstAlpha, BlendEquation colorEquation, BlendEquation alphaEquation) {}
}
