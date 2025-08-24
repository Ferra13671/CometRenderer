package com.ferra13671.cometrenderer.program.uniform.uniforms;

import com.ferra13671.TextureUtils.GlTex;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.uniform.GlUniform;
import com.mojang.blaze3d.opengl.GlConst;
import com.mojang.blaze3d.opengl.GlStateManager;
import net.minecraft.client.texture.GlTexture;
import net.minecraft.client.texture.GlTextureView;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/*
 * Униформа, хранящая в себе текстуру
 */
public class SamplerUniform extends GlUniform {
    private final int samplerId;
    private Object value;

    public SamplerUniform(String name, int location, GlProgram glProgram) {
        super(name, location, glProgram);

        samplerId = glProgram.getSamplersAmount() + 1;
        glProgram.setSamplersAmount(samplerId);
    }

    public void set(GlTex texture) {
        value = texture;
    }

    public void set(GlTextureView textureView) {
        value = textureView;
    }

    public int getSamplerId() {
        return samplerId;
    }

    @Override
    public void upload() {
        if (value instanceof GlTex glTexture) {
            if (GlProgram.ACTIVE_PROGRAM == null)
                GlStateManager._glUniform1i(location, getSamplerId());

            GlStateManager._activeTexture(GlConst.GL_TEXTURE0 + getSamplerId());
            GlStateManager._bindTexture(glTexture.getTexId());
        } else
        if (value instanceof GlTextureView glTextureView) {
            if (GlProgram.ACTIVE_PROGRAM == null)
                GlStateManager._glUniform1i(location, getSamplerId());

            GlStateManager._activeTexture(GlConst.GL_TEXTURE0 + getSamplerId());
            GlTexture glTexture = glTextureView.texture();
            int o;
            if ((glTexture.usage() & 16) != 0) {
                o = 34067;
                GL11.glBindTexture(34067, glTexture.getGlId());
            } else {
                o = GlConst.GL_TEXTURE_2D;
                GlStateManager._bindTexture(glTexture.getGlId());
            }

            GlStateManager._texParameter(o, 33084, glTextureView.baseMipLevel());
            GlStateManager._texParameter(o, GL12.GL_TEXTURE_MAX_LEVEL, glTextureView.baseMipLevel() + glTextureView.mipLevels() - 1);
            glTexture.checkDirty(o);
        }
    }
}
