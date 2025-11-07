package com.ferra13671.cometrenderer.scissor;

import com.ferra13671.cometrenderer.CometRenderer;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.opengl.GL20;

/**
 * Объект, представляющий собой область, используемая ножницами в OpenGL для установки границы, за пределами которой при отрисовке пиксели будут проигнорированы.
 *
 * @param x координата области по X.
 * @param y координата области по Y.
 * @param width длина области по X.
 * @param height длина области по Y.
 */
public record ScissorRect(int x, int y, int width, int height) {

    /**
     * Устанавливает данную область активной для ножниц.
     */
    public void bind() {
        GL20.glScissor(this.x, this.y, this.width, this.height);
    }

    /**
     * Исправляет координаты и размеры области.
     *
     * @return исправлена область.
     */
    public ScissorRect fixRect() {
        int scale = CometRenderer.getScaleGetter().get();
        int frameBufferHeight = MinecraftClient.getInstance().getWindow().getFramebufferHeight();
        return new ScissorRect(
                this.x * scale,
                frameBufferHeight - ((this.y + this.height) * scale),
                this.width * scale,
                this.height * scale
        );
    }

    /**
     * Возвращает область, точки в которой соответствуют как данной, так и текущей области.
     *
     * @param other область, с которой надо найти область пересечения.
     * @return область пересечения двух областей.
     */
    public ScissorRect intersection(ScissorRect other) {
        int x1 = Math.max(this.x, other.x);
        int y1 = Math.max(this.y, other.y);
        int x2 = Math.min(this.x + this.width, other.x + other.width);
        int y2 = Math.min(this.y + this.height, other.y + other.height);

        return x1 < x2 && y1 < y2 ? new ScissorRect(x1, y1, x2 - x1, y2 - y1) : null;
    }
}
