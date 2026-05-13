package com.ferra13671.cometrenderer.minecraft.program;

import com.ferra13671.cometrenderer.CometLoaders;
import com.ferra13671.cometrenderer.glsl.GlProgram;
import org.apiguardian.api.API;

@API(status = API.Status.MAINTAINED, since = "2.2")
public class DefaultPrograms {
    public final GlProgram POSITION = CometLoaders.STRING.createProgramBuilder()
            .name("position")
            .shader(DefaultShaders.POSITION_VERTEX)
            .shader(DefaultShaders.POSITION_FRAGMENT)
            .build();

    public final GlProgram POSITION_COLOR = CometLoaders.STRING.createProgramBuilder()
            .name("position-color")
            .shader(DefaultShaders.POSITION_COLOR_VERTEX)
            .shader(DefaultShaders.POSITION_COLOR_FRAGMENT)
            .build();

    public final GlProgram POSITION_TEXTURE = CometLoaders.STRING.createProgramBuilder()
            .name("position-texture")
            .shader(DefaultShaders.POSITION_TEXTURE_VERTEX)
            .shader(DefaultShaders.POSITION_TEXTURE_FRAGMENT)
            .build();

    public final GlProgram POSITION_TEXTURE_COLOR = CometLoaders.STRING.createProgramBuilder()
            .name("position-texture-color")
            .shader(DefaultShaders.POSITION_TEXTURE_COLOR_VERTEX)
            .shader(DefaultShaders.POSITION_TEXTURE_COLOR_FRAGMENT)
            .build();

    public final GlProgram ROUNDED_RECT = CometLoaders.STRING.createProgramBuilder()
            .name("rounded-rect")
            .shader(DefaultShaders.ROUNDED_RECT_VERTEX)
            .shader(DefaultShaders.ROUNDED_RECT_FRAGMENT)
            .build();

    public final GlProgram ROUNDED_TEXTURE = CometLoaders.STRING.createProgramBuilder()
            .name("rounded-texture")
            .shader(DefaultShaders.ROUNDED_TEXTURE_VERTEX)
            .shader(DefaultShaders.ROUNDED_TEXTURE_FRAGMENT)
            .build();

    public final GlProgram BLUR_FRAME = CometLoaders.STRING.createProgramBuilder()
            .name("blur-frame")
            .shader(DefaultShaders.POSITION_VERTEX)
            .shader(DefaultShaders.BLUR_FRAME_FRAGMENT)
            .build();

    public final GlProgram ROUNDED_BLUR = CometLoaders.STRING.createProgramBuilder()
            .name("rounded-blur")
            .shader(DefaultShaders.ROUNDED_BLUR_VERTEX)
            .shader(DefaultShaders.ROUNDED_BLUR_FRAGMENT)
            .build();

    public final GlProgram BLIT = CometLoaders.STRING.createProgramBuilder()
            .name("blit")
            .shader(DefaultShaders.POSITION_VERTEX)
            .shader(DefaultShaders.BLIT_FRAGMENT)
            .build();
}
