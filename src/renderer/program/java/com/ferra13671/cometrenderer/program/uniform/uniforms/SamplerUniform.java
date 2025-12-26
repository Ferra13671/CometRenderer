package com.ferra13671.cometrenderer.program.uniform.uniforms;

import com.ferra13671.cometrenderer.State;
import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.uniform.GlUniform;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import com.ferra13671.ferraguard.annotations.OverriddenMethod;
import com.ferra13671.gltextureutils.GlTex;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.function.BiConsumer;

/**
 * Униформа, хранящая в себе параметр в виде текстуры.
 *
 * @see GlUniform
 * @see UniformType
 */
public class SamplerUniform extends GlUniform {
    /** Айди семплера. **/
    private final int samplerId;
    /** Runnable, загружающий параметр в униформу. **/
    private Runnable uploadRunnable = null;

    /**
     * @param name имя униформы.
     * @param location локация униформы в OpenGL.
     * @param glProgram программа ({@link GlProgram}), к которой привязана униформа.
     */
    public SamplerUniform(String name, int location, GlProgram glProgram) {
        super(name, location, glProgram);

        this.samplerId = glProgram.getSamplersAmount() + 1;
        glProgram.setSamplersAmount(this.samplerId);
    }

    /**
     * Устанавливает текстуру из GlTex.
     *
     * @param texture GlTex.
     *
     * @see GlTex
     */
    public void set(GlTex texture) {
        this.uploadRunnable = () -> {
            State.TEXTURE.activeTexture(GL13.GL_TEXTURE0 + this.samplerId);
            State.TEXTURE.bindTexture(texture.getTexId());
        };
        this.program.addUpdatedUniform(this);
    }

    /**
     * Устанавливает текстуру при помощи её айди в OpenGL.
     *
     * @param textureId айди текстуры в OpenGL.
     */
    public void set(int textureId) {
        this.uploadRunnable = () -> {
            State.TEXTURE.activeTexture(GL30.GL_TEXTURE0 + this.samplerId);
            State.TEXTURE.bindTexture(textureId);
        };
        this.program.addUpdatedUniform(this);
    }

    /**
     * Устанавливает текстуру при помощи пользовательского установщика и объекта.
     *
     * @param uploadConsumer установщик текстуры.
     * @param texture объект текстуры.
     * @param <T> текстура.
     */
    public <T> void set(BiConsumer<SamplerUniform, T> uploadConsumer, T texture) {
        this.uploadRunnable = () -> uploadConsumer.accept(this, texture);
        this.program.addUpdatedUniform(this);
    }

    /**
     * Возвращает айди семплера.
     *
     * @return айди семплера.
     */
    public int getSamplerId() {
        return this.samplerId;
    }

    @Override
    @OverriddenMethod
    public void upload() {
        if (this.uploadRunnable != null) {
            GL20.glUniform1i(this.location, getSamplerId());

            this.uploadRunnable.run();
        }
    }
}
