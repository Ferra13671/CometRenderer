package com.ferra13671.cometrenderer.texture;

import lombok.AllArgsConstructor;
import org.apiguardian.api.API;
import org.lwjgl.opengl.GL11;

@API(status = API.Status.EXPERIMENTAL, since = "3.0")
@AllArgsConstructor
public enum TextureFiltering {
    DEFAULT(GL11.GL_NEAREST),
    SMOOTH(GL11.GL_LINEAR);

    public final int id;
}
