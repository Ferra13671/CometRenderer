package com.ferra13671.cometrenderer.sampler.impl;

import com.ferra13671.cometrenderer.sampler.ISampler;
import com.ferra13671.gltextureutils.TextureFiltering;
import com.ferra13671.gltextureutils.TextureWrapping;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL33;

public class SamplerImpl implements ISampler {
    private final int id;

    public SamplerImpl() {
        this.id = GL33.glGenSamplers();
    }

    @Override
    public void setFiltering(TextureFiltering filtering) {
        GL33.glSamplerParameteri(this.id, GL11.GL_TEXTURE_MIN_FILTER, filtering.id);
        GL33.glSamplerParameteri(this.id, GL11.GL_TEXTURE_MAG_FILTER, filtering.id);
    }

    @Override
    public void setWrapping(TextureWrapping wrapping) {
        GL33.glSamplerParameteri(this.id, GL11.GL_TEXTURE_WRAP_S, wrapping.id);
        GL33.glSamplerParameteri(this.id, GL11.GL_TEXTURE_WRAP_T, wrapping.id);
    }

    public int getId() {
        return this.id;
    }
}
