package com.ferra13671.cometrenderer.buffer.framebuffer;

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
    public boolean useDepth = true;
    @Builder.Default
    public boolean useStencil = true;
    public final int width;
    public final int height;
    @Builder.Default
    public Color clearColor = new Color(255, 255, 255, 0);
    @Builder.Default
    public double clearDepth = 1;
    @Builder.Default
    public int clearStencil = 0;
}
