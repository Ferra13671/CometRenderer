package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FramebufferUtils {

    public void resizeToParent(Framebuffer framebuffer, Framebuffer parent) {
        int width = parent.getWidth();
        int height = parent.getHeight();

        if (framebuffer.getWidth() != width || framebuffer.getHeight() != height) {
            framebuffer.resize(width, height);
            framebuffer.clearAll();
        }
    }
}
