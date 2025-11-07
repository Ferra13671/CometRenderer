package com.ferra13671.cometrenderer.posteffect;

import com.ferra13671.cometrenderer.buffer.framebuffer.CometFrameBuffer;
import com.ferra13671.cometrenderer.vertex.mesh.Mesh;

import java.util.HashMap;

/**
 * Контекст пост эффекта, передаваемый пассам для их отрисовки.
 *
 * @param mesh меш для отрисовки пасса на весь фреймбуффер.
 * @param framebuffers список локальных фреймбуфферов пост эффекта.
 */
public record PostEffectContext(Mesh mesh, HashMap<String, CometFrameBuffer> framebuffers) {
}
