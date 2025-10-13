package com.ferra13671.cometrenderer.scissor;

import com.ferra13671.cometrenderer.CometRenderer;
import com.mojang.blaze3d.opengl.GlStateManager;
import net.minecraft.client.MinecraftClient;

/*
 * Область ножниц, используемая в OpenGL для того, что бы задавать определенную область, за пределами которой при рендере пиксели не будут затронуты.
 */
public record ScissorRect(int x, int y, int width, int height) {

    /*
     * Биндит область ножниц
     */
    public void bind() {
        GlStateManager._scissorBox(x, y, width, height);
    }

    public ScissorRect fixRect() {
        int scale = CometRenderer.getScaleGetter().get();
        int frameBufferHeight = MinecraftClient.getInstance().getWindow().getFramebufferHeight();
        return new ScissorRect(
                x * scale,
                frameBufferHeight - ((y + height) * scale),
                width * scale,
                height * scale
        );
    }

    public ScissorRect intersection(ScissorRect other) {
        int x1 = Math.max(this.x, other.x);
        int y1 = Math.max(this.y, other.y);
        int x2 = Math.min(this.x + this.width, other.x + other.width);
        int y2 = Math.min(this.y + this.height, other.y + other.height);

        return x1 < x2 && y1 < y2 ? new ScissorRect(x1, y1, x2 - x1, y2 - y1) : null;
    }
}
