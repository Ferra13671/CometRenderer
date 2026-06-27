package com.ferra13671.cometrenderer.minecraft.program;

import com.ferra13671.cometrenderer.glsl.GLProgram;
import com.ferra13671.cometrenderer.glsl.GLProgramBuilder;
import org.apiguardian.api.API;

@API(status = API.Status.MAINTAINED, since = "2.2")
public class DefaultPrograms {
    public final GLProgram POSITION = new GLProgramBuilder<>()
            .name("position")
            .shader(DefaultShaders.POSITION_VERTEX)
            .shader(DefaultShaders.POSITION_FRAGMENT)
            .build();

    public final GLProgram POSITION_COLOR = new GLProgramBuilder<>()
            .name("position-color")
            .shader(DefaultShaders.POSITION_COLOR_VERTEX)
            .shader(DefaultShaders.POSITION_COLOR_FRAGMENT)
            .build();

    public final GLProgram POSITION_TEXTURE = new GLProgramBuilder<>()
            .name("position-texture")
            .shader(DefaultShaders.POSITION_TEXTURE_VERTEX)
            .shader(DefaultShaders.POSITION_TEXTURE_FRAGMENT)
            .build();

    public final GLProgram POSITION_TEXTURE_COLOR = new GLProgramBuilder<>()
            .name("position-texture-color")
            .shader(DefaultShaders.POSITION_TEXTURE_COLOR_VERTEX)
            .shader(DefaultShaders.POSITION_TEXTURE_COLOR_FRAGMENT)
            .build();

    public final GLProgram ROUNDED_RECT = new GLProgramBuilder<>()
            .name("rounded-rect")
            .shader(DefaultShaders.ROUNDED_RECT_VERTEX)
            .shader(DefaultShaders.ROUNDED_RECT_FRAGMENT)
            .build();

    public final GLProgram ROUNDED_TEXTURE = new GLProgramBuilder<>()
            .name("rounded-texture")
            .shader(DefaultShaders.ROUNDED_TEXTURE_VERTEX)
            .shader(DefaultShaders.ROUNDED_TEXTURE_FRAGMENT)
            .build();

    public final GLProgram BLUR_FRAME = new GLProgramBuilder<>()
            .name("blur-frame")
            .shader(DefaultShaders.POSITION_VERTEX)
            .shader(DefaultShaders.BLUR_FRAME_FRAGMENT)
            .build();

    public final GLProgram ROUNDED_BLUR = new GLProgramBuilder<>()
            .name("rounded-blur")
            .shader(DefaultShaders.ROUNDED_BLUR_VERTEX)
            .shader(DefaultShaders.ROUNDED_BLUR_FRAGMENT)
            .build();

    public final GLProgram BLIT = new GLProgramBuilder<>()
            .name("blit")
            .shader(DefaultShaders.POSITION_VERTEX)
            .shader(DefaultShaders.BLIT_FRAGMENT)
            .build();

    public final GLProgram LIQUID_GLASS = new GLProgramBuilder<>()
            .name("liquid-glass")
            .shader(DefaultShaders.ROUNDED_RECT_VERTEX)
            .shader(DefaultShaders.LIQUID_GLASS_FRAGMENT)
            .build();
}
