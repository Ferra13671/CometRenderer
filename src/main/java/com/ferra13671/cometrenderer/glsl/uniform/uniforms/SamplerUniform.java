package com.ferra13671.cometrenderer.glsl.uniform.uniforms;

import com.ferra13671.cometrenderer.sampler.GLSampler;
import com.ferra13671.cometrenderer.glsl.uniform.GLUniform;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import com.ferra13671.cometrenderer.sampler.unit.SamplerUnitBindable;
import com.ferra13671.cometrenderer.sampler.unit.TextureUnitBindable;
import com.ferra13671.cometrenderer.sampler.unit.UnitBindable;
import com.ferra13671.gltextureutils.GlTex;
import lombok.Getter;
import lombok.Setter;
import org.apiguardian.api.API;
import org.lwjgl.opengl.GL20;

/**
 * Униформа, хранящая в себе параметр в виде текстуры.
 *
 * @see GLUniform
 * @see UniformType
 */
@API(status = API.Status.STABLE, since = "1.1")
public class SamplerUniform extends GLUniform {
    /** Айди семплера. **/
    @Getter
    @Setter
    private int unit;
    private UnitBindable textureBindable = TextureUnitBindable.EMPTY;
    private UnitBindable samplerBindable = SamplerUnitBindable.EMPTY;

    /**
     * @param name имя униформы.
     * @param location локация униформы в OpenGL.
     */
    public SamplerUniform(String name, int location) {
        super(name, location);

        GL20.glUniform1i(this.location, getUnit());
    }

    @API(status = API.Status.MAINTAINED, since = "3.0")
    public void setTexture(GlTex texture) {
        setTexture(texture == null ? 0 : texture.getTexId());
    }

    @API(status = API.Status.MAINTAINED, since = "3.0")
    public void setTexture(int textureId) {
        setTexture(textureId == 0 ? TextureUnitBindable.EMPTY : new TextureUnitBindable(textureId));
    }

    @API(status = API.Status.MAINTAINED, since = "3.0")
    public void setTexture(UnitBindable bindable) {
        this.textureBindable = bindable;
    }

    @API(status = API.Status.MAINTAINED, since = "3.0")
    public void setSampler(GLSampler sampler) {
        setSampler(sampler == null ? 0 : sampler.getId());
    }

    @API(status = API.Status.MAINTAINED, since = "3.0")
    public void setSampler(int samplerId) {
        setSampler(samplerId == 0 ? SamplerUnitBindable.EMPTY : new SamplerUnitBindable(samplerId));
    }

    @API(status = API.Status.MAINTAINED, since = "3.0")
    public void setSampler(UnitBindable bindable) {
        this.samplerBindable = bindable;
    }

    @API(status = API.Status.MAINTAINED, since = "3.0")
    public void setTextureSampler(GlTex texture, GLSampler sampler) {
        setTexture(texture);
        setSampler(sampler);
    }

    @API(status = API.Status.MAINTAINED, since = "3.0")
    public void setTextureSampler(int[] ids) {
        setTextureSampler(ids[0], ids[1]);
    }

    @API(status = API.Status.MAINTAINED, since = "3.0")
    public void setTextureSampler(UnitBindable[] bindables) {
        setTextureSampler(bindables[0], bindables[1]);
    }

    @API(status = API.Status.MAINTAINED, since = "3.0")
    public void setTextureSampler(int textureId, int samplerId) {
        setTexture(textureId);
        setSampler(samplerId);
    }

    @API(status = API.Status.MAINTAINED, since = "3.0")
    public void setTextureSampler(UnitBindable textureBindable, UnitBindable samplerBindable) {
        setTexture(textureBindable);
        setSampler(samplerBindable);
    }

    @Override
    public void upload() {
        this.textureBindable.bind(getUnit());
        this.samplerBindable.bind(getUnit());
    }
}
