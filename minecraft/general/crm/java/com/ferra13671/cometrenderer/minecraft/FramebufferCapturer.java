package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.cometrenderer.buffer.framebuffer.FramebufferImpl;
import com.ferra13671.cometrenderer.buffer.framebuffer.FramebufferInfo;
import org.apiguardian.api.API;

import java.awt.*;

@API(status = API.Status.MAINTAINED, since = "2.9")
public class FramebufferCapturer implements HasFramebuffer {
    private final FramebufferImpl capturedFramebuffer = new FramebufferImpl(
            FramebufferInfo.builder()
                    .name("Captured framebuffer")
                    .useDepth(false)
                    .clearColor(Color.blue)
                    .width(1)
                    .height(1)
                    .build()
    );

    @Override
    public Framebuffer getFramebuffer() {
        return this.capturedFramebuffer;
    }

    public void capture(Framebuffer framebuffer) {
        FramebufferUtils.resizeToParent(this.capturedFramebuffer, framebuffer);

        CRM.getMainFramebuffer().blit(this.capturedFramebuffer, true, true);
        CRM.getMainFramebuffer().bind(true);
    }
}
