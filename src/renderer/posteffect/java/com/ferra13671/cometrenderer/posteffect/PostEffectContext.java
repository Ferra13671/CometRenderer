package com.ferra13671.cometrenderer.posteffect;

import com.ferra13671.cometrenderer.framebuffer.CometFrameBuffer;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;

import java.util.HashMap;

/*
 * Контекст пост эффекта.
 * Хранит в себе построенный буффер вершин и список всех локальных фреймбуфферов.
 */
public record PostEffectContext(Mesh buffer, HashMap<String, CometFrameBuffer> framebuffers) {
}
