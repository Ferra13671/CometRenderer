package com.ferra13671.cometrenderer.minecraft.program;

import com.ferra13671.cometrenderer.glsl.GLProgram;
import org.apiguardian.api.API;

@API(status = API.Status.MAINTAINED, since = "2.2")
public class DefaultPrograms {
    public final GLProgram POSITION = GLProgram.builder()
            .name("position")
            .shader(DefaultShaders.POSITION_VERTEX)
            .shader(DefaultShaders.POSITION_FRAGMENT)
            .build();

    public final GLProgram POSITION_COLOR = GLProgram.builder()
            .name("position-color")
            .shader(DefaultShaders.POSITION_COLOR_VERTEX)
            .shader(DefaultShaders.POSITION_COLOR_FRAGMENT)
            .build();

    public final GLProgram POSITION_TEXTURE = GLProgram.builder()
            .name("position-texture")
            .shader(DefaultShaders.POSITION_TEXTURE_VERTEX)
            .shader(DefaultShaders.POSITION_TEXTURE_FRAGMENT)
            .build();

    public final GLProgram POSITION_TEXTURE_COLOR = GLProgram.builder()
            .name("position-texture-color")
            .shader(DefaultShaders.POSITION_TEXTURE_COLOR_VERTEX)
            .shader(DefaultShaders.POSITION_TEXTURE_COLOR_FRAGMENT)
            .build();

    public final GLProgram ROUNDED_RECT = GLProgram.builder()
            .name("rounded-rect")
            .shader(DefaultShaders.ROUNDED_RECT_VERTEX)
            .shader(DefaultShaders.ROUNDED_RECT_FRAGMENT)
            .build();

    public final GLProgram ROUNDED_TEXTURE = GLProgram.builder()
            .name("rounded-texture")
            .shader(DefaultShaders.ROUNDED_TEXTURE_VERTEX)
            .shader(DefaultShaders.ROUNDED_TEXTURE_FRAGMENT)
            .build();

    public final GLProgram BLUR_FRAME = GLProgram.builder()
            .name("blur-frame")
            .shader(DefaultShaders.POSITION_VERTEX)
            .shader(DefaultShaders.BLUR_FRAME_FRAGMENT)
            .build();

    public final GLProgram ROUNDED_BLUR = GLProgram.builder()
            .name("rounded-blur")
            .shader(DefaultShaders.ROUNDED_BLUR_VERTEX)
            .shader(DefaultShaders.ROUNDED_BLUR_FRAGMENT)
            .build();

    public final GLProgram BLIT = GLProgram.builder()
            .name("blit")
            .shader(DefaultShaders.POSITION_VERTEX)
            .shader(DefaultShaders.BLIT_FRAGMENT)
            .build();

    public final GLProgram LIQUID_GLASS = GLProgram.builder()
            .name("liquid-glass")
            .shader(DefaultShaders.ROUNDED_RECT_VERTEX)
            .shader(DefaultShaders.LIQUID_GLASS_FRAGMENT)
            .build();
}
