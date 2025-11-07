package com.ferra13671.cometrenderer.posteffect;

/**
 * Информация о локальном фреймбуффере пост эффекта.
 *
 * @param name имя фреймбуффера.
 * @param clearColor цвет очистки фреймбуффера.
 *
 * @see PostEffectPipeline
 */
public record LocalFrameBufferInfo(String name, int clearColor) {
}
