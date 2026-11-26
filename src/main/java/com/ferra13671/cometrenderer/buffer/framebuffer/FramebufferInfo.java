package com.ferra13671.cometrenderer.buffer.framebuffer;

import com.ferra13671.cometrenderer.CometRenderer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.awt.*;

@Builder
@Getter
@AllArgsConstructor
public class FramebufferInfo {
    public final String name;
    @Builder.Default
    public boolean useColor = true;
    @Builder.Default
    public boolean useDepth = true;
    @Builder.Default
    public boolean useStencil = true;
    public final int width;
    public final int height;
    @Builder.Default
    public Color clearColor = CometRenderer.getConfig().getDefaultClearColor();
    @Builder.Default
    public double clearDepth = CometRenderer.getConfig().getDefaultClearDepth();
    @Builder.Default
    public int clearStencil = CometRenderer.getConfig().getDefaultClearStencil();
}
