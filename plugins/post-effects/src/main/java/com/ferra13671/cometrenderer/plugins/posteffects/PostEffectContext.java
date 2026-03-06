package com.ferra13671.cometrenderer.plugins.posteffects;

import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import com.ferra13671.cometrenderer.vertex.mesh.IMesh;

import java.util.HashMap;

public record PostEffectContext(IMesh mesh, HashMap<String, Framebuffer> framebuffers) {
}
