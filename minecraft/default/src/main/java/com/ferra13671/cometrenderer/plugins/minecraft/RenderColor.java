package com.ferra13671.cometrenderer.plugins.minecraft;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.joml.Vector4f;

import java.awt.*;

//Да, тут будет использоваться своя реализация цвета, сосите лол
@AllArgsConstructor
@Getter
public class RenderColor {
    public static final RenderColor TRANSLUCENT = RenderColor.of(0f, 0f, 0f, 0f);
    public static final RenderColor WHITE = RenderColor.ofRGBA(-1);
    public static final RenderColor BLACK = RenderColor.of(0, 0, 0, 255);

    private float[] color;

    public RenderColor multiply(RenderColor renderColor) {
        return RenderColor.of(
                this.color[0] * renderColor.getColor()[0],
                this.color[1] * renderColor.getColor()[1],
                this.color[2] * renderColor.getColor()[2],
                this.color[3] * renderColor.getColor()[3]
        );
    }

    public Vector4f toVector4f() {
        return new Vector4f(
                this.color[0],
                this.color[1],
                this.color[2],
                this.color[3]
        );
    }

    public static RenderColor of(Color color) {
        return of(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                color.getAlpha()
        );
    }

    public static RenderColor of(int red, int green, int blue, int alpha) {
        return of(
                red / 255f,
                green / 255f,
                blue / 255f,
                alpha / 255f
        );
    }

    public static RenderColor of(float red, float green, float blue, float alpha) {
        return new RenderColor(new float[]{
                red,
                green,
                blue,
                alpha
        });
    }

    public static RenderColor ofRGBA(int rgba) {
        return new RenderColor(new float[]{
                (rgba >> 16 & 255) / 255f,
                (rgba >> 8 & 255) / 255f,
                (rgba & 255) / 255f,
                (rgba >>> 24) / 255f
        });
    }

    public static RenderColor ofRGB(int rgb) {
        return new RenderColor(new float[]{
                (rgb >> 16 & 255) / 255f,
                (rgb >> 8 & 255) / 255f,
                (rgb & 255) / 255f,
                1f
        });
    }
}
