package com.ferra13671.cometrenderer.program.uniform.uniforms.sampler;

import com.ferra13671.TextureUtils.GlTex;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.uniform.GlUniform;
import com.ferra13671.ferraguard.annotations.OverriddenMethod;
import com.mojang.blaze3d.opengl.GlStateManager;
import net.minecraft.client.texture.GlTextureView;

import java.util.function.Consumer;

/*
 * Униформа, хранящая в себе текстуру
 */
public class SamplerUniform extends GlUniform {
    private final int samplerId;
    private Consumer<SamplerUniform> applier;

    public SamplerUniform(String name, int location, GlProgram glProgram) {
        super(name, location, glProgram);

        this.samplerId = glProgram.getSamplersAmount() + 1;
        glProgram.setSamplersAmount(this.samplerId);
    }

    /*
     * Устанавливает текстуру из GlTex
     */
    public void set(GlTex texture) {
        this.applier = SamplerAppliers.GL_TEX.apply(texture);
    }

    /*
     * Устанавливает текстуру из GlTextureView
     */
    public void set(GlTextureView textureView) {
        this.applier = SamplerAppliers.GL_TEXTURE_VIEW.apply(textureView);
    }

    /*
     * Устанавливает текстуру при помощи её андишника в OpenGL
     */
    public void set(int textureId) {
        this.applier = SamplerAppliers.TEXTURE_ID.apply(textureId);
    }

    /*
     * Устанавливает текстуру кастомным установщиком
     */
    public void set(Consumer<SamplerUniform> applier) {
        this.applier = applier;
    }

    public int getSamplerId() {
        return this.samplerId;
    }

    @Override
    @OverriddenMethod
    public void upload() {
        if (GlProgram.ACTIVE_PROGRAM == null)
            GlStateManager._glUniform1i(this.location, getSamplerId());

        //В зависимости от установщика устанавливаем текстуру в семплер
        this.applier.accept(this);
    }
}
