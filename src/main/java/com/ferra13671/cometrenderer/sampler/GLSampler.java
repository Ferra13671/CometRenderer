package com.ferra13671.cometrenderer.sampler;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.texture.TextureFiltering;
import com.ferra13671.cometrenderer.texture.TextureWrapping;
import lombok.Getter;
import org.apiguardian.api.API;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL33;

import java.io.Closeable;

@API(status = API.Status.MAINTAINED, since = "3.0")
@Getter
public class GLSampler implements Closeable {
    private final int id;

    public GLSampler() {
        this.id = CometRenderer.getDevice().createSampler();
    }

    public void setFiltering(TextureFiltering filtering) {
        GL33.glSamplerParameteri(this.id, GL11.GL_TEXTURE_MIN_FILTER, filtering.id);
        GL33.glSamplerParameteri(this.id, GL11.GL_TEXTURE_MAG_FILTER, filtering.id);
    }

    public void setWrapping(TextureWrapping wrapping) {
        GL33.glSamplerParameteri(this.id, GL11.GL_TEXTURE_WRAP_S, wrapping.id);
        GL33.glSamplerParameteri(this.id, GL11.GL_TEXTURE_WRAP_T, wrapping.id);
    }

    @Override
    public void close() {
        CometRenderer.getDevice().deleteSampler(getId());
    }
}
