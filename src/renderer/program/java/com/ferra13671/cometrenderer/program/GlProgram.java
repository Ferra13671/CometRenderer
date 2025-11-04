package com.ferra13671.cometrenderer.program;

import com.ferra13671.cometrenderer.Bindable;
import com.ferra13671.cometrenderer.exceptions.ExceptionPrinter;
import com.ferra13671.cometrenderer.exceptions.impl.NoSuchUniformException;
import com.ferra13671.cometrenderer.builders.GlUniformSchema;
import com.ferra13671.cometrenderer.program.uniform.GlUniform;
import com.ferra13671.cometrenderer.program.uniform.UniformType;
import com.ferra13671.cometrenderer.program.uniform.uniforms.buffer.BufferUniform;
import com.ferra13671.cometrenderer.program.uniform.uniforms.sampler.SamplerUniform;
import com.ferra13671.ferraguard.annotations.OverriddenMethod;
import org.lwjgl.opengl.GL20;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GlProgram implements Bindable {
    public static GlProgram ACTIVE_PROGRAM = null;

    private final String name;
    private final int id;
    private final List<GlUniform> uniforms = new ArrayList<>();
    private final HashMap<String, GlUniform> uniformsByName = new HashMap<>();
    private final List<SamplerUniform> samplers = new ArrayList<>();
    //Количество семплеров (Юниформ с типом SAMPLER)
    private int samplersAmount = 0;
    //Количество забинженных буфферов (Юниформ с типом BUFFER)
    private int buffersBindingsAmount = 0;

    public GlProgram(String name, int id, List<GlUniformSchema<?>> uniforms) {
        this.name = name;
        this.id = id;

        for (GlUniformSchema<?> glUniformSchema : uniforms) {
            GlUniform uniform = glUniformSchema.uniformType().uniformCreator().apply(
                    glUniformSchema.name(),
                    glUniformSchema.getIdFromProgram(this.id),
                    this
            );

            if (uniform.getLocation() == -1 && !(uniform instanceof BufferUniform))
                ExceptionPrinter.printAndExit(new NoSuchUniformException(uniform.getName(), this.name));

            this.uniforms.add(uniform);
            this.uniformsByName.put(glUniformSchema.name(), uniform);

            if (uniform instanceof SamplerUniform sampler)
                samplers.add(sampler);
        }
    }

    /*
     * Привязывает к OpenGl программу
     */
    @Override
    @OverriddenMethod
    public void bind() {
        GL20.glUseProgram(getId());

        for (GlUniform glUniform : uniforms)
            glUniform.upload();

        ACTIVE_PROGRAM = this;
    }

    /*
     * Отвязывает с OpenGl программу
     */
    @Override
    @OverriddenMethod
    public void unbind() {
        GL20.glUseProgram(0);

        ACTIVE_PROGRAM = null;
    }

    /*
     * Возвращает имя программы
     */
    public String getName() {
        return name;
    }

    /*
     * Возвращает айди программы в OpenGL
     */
    public int getId() {
        return id;
    }

    /*
     * Возвращает юниформу по её имени и типу, если юниформа не была найдена, то выдем ошибку
     */
    public <T extends GlUniform> T getUniform(String name, UniformType<T> type) {
        T uniform = getUniformNullable(name, type);
        if (uniform == null)
            ExceptionPrinter.printAndExit(new NoSuchUniformException(name, this.name));
        return uniform;
    }

    /*
     * Возвращает юниформу по её имени и типу
     */
    public <T extends GlUniform> T getUniformNullable(String name, UniformType<T> type) {
        return (T) uniformsByName.get(name);
    }

    /*
     * Возвращает семплер по его айдишнику
     */
    public SamplerUniform getSampler(int samplerId) {
        SamplerUniform sampler = samplers.get(samplerId);
        if (sampler == null)
            ExceptionPrinter.printAndExit(new NoSuchUniformException("Sampler[" + samplerId + "]", this.name));
        return sampler;
    }

    /*
     * Возвращает количество забинженных буфферов в программе
     */
    public int getBuffersBindingsAmount() {
        return buffersBindingsAmount;
    }

    /*
     * Устанавливает количество забинженных буфферов в программе
     */
    public void setBuffersBindingsAmount(int buffersBindingsAmount) {
        this.buffersBindingsAmount = buffersBindingsAmount;
    }

    /*
     * Возвращает количество семплеров в программе
     */
    public int getSamplersAmount() {
        return samplersAmount;
    }

    /*
     * Устанавливает количество семплеров в программе
     */
    public void setSamplersAmount(int samplersAmount) {
        this.samplersAmount = samplersAmount;
    }
}
