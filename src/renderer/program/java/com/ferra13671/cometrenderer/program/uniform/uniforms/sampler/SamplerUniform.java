package com.ferra13671.cometrenderer.program.uniform.uniforms.sampler;

import com.ferra13671.TextureUtils.GlTex;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.uniform.GlUniform;
import com.ferra13671.ferraguard.annotations.OverriddenMethod;
import com.mojang.blaze3d.opengl.GlStateManager;
import net.minecraft.client.texture.GlTextureView;

/*
 * Униформа, хранящая в себе текстуру
 */
public class SamplerUniform extends GlUniform {
    private final int samplerId;
    private Runnable uploadRunnable = null;

    public SamplerUniform(String name, int location, GlProgram glProgram) {
        super(name, location, glProgram);

        this.samplerId = glProgram.getSamplersAmount() + 1;
        glProgram.setSamplersAmount(this.samplerId);
    }

    /*
     * Устанавливает текстуру из GlTex
     */
    public void set(GlTex texture) {
        this.uploadRunnable = () -> SamplerUniformApplier.GL_TEX.applyConsumer().accept(this, texture);
    }

    /*
     * Устанавливает текстуру из GlTextureView
     */
    public void set(GlTextureView textureView) {
        this.uploadRunnable = () -> SamplerUniformApplier.GL_TEXTURE_VIEW.applyConsumer().accept(this, textureView);
    }

    /*
     * Устанавливает текстуру при помощи её андишника в OpenGL
     */
    public void set(int textureId) {
        this.uploadRunnable = () -> SamplerUniformApplier.TEXTURE_ID.applyConsumer().accept(this, textureId);
    }

    /*
     * Устанавливает текстуру кастомным установщиком
     */
    public <T> void set(SamplerUniformApplier<T> applier, T texture) {
        this.uploadRunnable = () -> applier.applyConsumer().accept(this, texture);
    }

    public int getSamplerId() {
        return this.samplerId;
    }

    @Override
    @OverriddenMethod
    public void upload() {
        if (this.uploadRunnable != null) {
            if (GlProgram.ACTIVE_PROGRAM == null)
                GlStateManager._glUniform1i(this.location, getSamplerId());

            //В зависимости от установщика устанавливаем текстуру в семплер
            this.uploadRunnable.run();
        }
    }
}
