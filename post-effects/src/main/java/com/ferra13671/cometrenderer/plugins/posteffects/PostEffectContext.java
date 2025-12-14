package com.ferra13671.cometrenderer.plugins.posteffects;

import com.ferra13671.cometrenderer.buffer.framebuffer.CometFrameBuffer;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;

import java.util.HashMap;

public record PostEffectContext(Mesh mesh, HashMap<String, CometFrameBuffer> framebuffers) {
}
