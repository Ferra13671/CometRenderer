package com.ferra13671.cometrenderer.texture;

import lombok.AllArgsConstructor;
import org.apiguardian.api.API;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;

@API(status = API.Status.EXPERIMENTAL, since = "3.0")
@AllArgsConstructor
public enum TextureWrapping {
    DEFAULT(GL12.GL_CLAMP_TO_EDGE),
    REPEAT(GL11.GL_REPEAT),
    MIRROR_REPEAT(GL14.GL_MIRRORED_REPEAT);

    public final int id;
}
