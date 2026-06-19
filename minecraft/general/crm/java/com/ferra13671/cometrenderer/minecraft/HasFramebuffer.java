package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import org.apiguardian.api.API;

@API(status = API.Status.MAINTAINED, since = "2.9")
public interface HasFramebuffer {

    Framebuffer getFramebuffer();
}
