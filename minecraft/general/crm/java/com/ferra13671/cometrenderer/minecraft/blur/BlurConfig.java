package com.ferra13671.cometrenderer.minecraft.blur;

import lombok.AllArgsConstructor;
import org.joml.Vector2f;

@AllArgsConstructor
public class BlurConfig {
    public static final BlurConfig DEFAULT = new BlurConfig(new BlurPass[]{
            new BlurPass(new Vector2f(10, 0), 3),
            new BlurPass(new Vector2f(4, 0), 3),
            new BlurPass(new Vector2f(1, 0), 3),
            new BlurPass(new Vector2f(0, 10), 3),
            new BlurPass(new Vector2f(0, 4), 3),
            new BlurPass(new Vector2f(0, 1), 3)
    });

    protected final BlurPass[] passes;
}
