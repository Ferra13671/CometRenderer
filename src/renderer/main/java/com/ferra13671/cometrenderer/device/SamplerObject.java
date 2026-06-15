package com.ferra13671.cometrenderer.device;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.gltextureutils.TextureFiltering;
import com.ferra13671.gltextureutils.TextureWrapping;
import lombok.Getter;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL33;

@Getter
public class SamplerObject {
    private final int id;

    public SamplerObject() {
        this.id = CometRenderer.getDevice().getDirectStateManager().createSampler();
    }

    public void setFiltering(TextureFiltering filtering) {
        GL33.glSamplerParameteri(this.id, GL11.GL_TEXTURE_MIN_FILTER, filtering.id);
        GL33.glSamplerParameteri(this.id, GL11.GL_TEXTURE_MAG_FILTER, filtering.id);
    }

    public void setWrapping(TextureWrapping wrapping) {
        GL33.glSamplerParameteri(this.id, GL11.GL_TEXTURE_WRAP_S, wrapping.id);
        GL33.glSamplerParameteri(this.id, GL11.GL_TEXTURE_WRAP_T, wrapping.id);
    }
}
