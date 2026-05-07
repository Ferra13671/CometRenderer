package com.ferra13671.cometrenderer.plugins.posteffects;

import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.cometrenderer.vertex.mesh.IMesh;
import org.apiguardian.api.API;

import java.util.HashMap;

@API(status = API.Status.MAINTAINED)
public record PostEffectContext(IMesh mesh, HashMap<String, Framebuffer> framebuffers) {
}
