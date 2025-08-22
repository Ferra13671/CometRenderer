package com.ferra13671.cometrenderer.program.uniform;

import com.ferra13671.cometrenderer.program.GlProgram;

/*
 * Объект, хранящий и передающий аргумент юниформы, которая в свою очередь передает его программе OpenGL
 */
public abstract class GlUniform {
    protected final String name;
    protected final int location;
    protected final GlProgram glProgram;

    public GlUniform(String name, int location, GlProgram glProgram) {
        this.name = name;
        this.location = location;
        this.glProgram = glProgram;
    }

    /*
     * Возвращает имя юниформы
     */
    public String getName() {
        return name;
    }

    /*
     * Возвращает локацию юниформы
     */
    public int getLocation() {
        return location;
    }

    /*
     * Возвращает программу, к которой привязана юниформа
     */
    public GlProgram getGlProgram() {
        return glProgram;
    }

    /*
     * Загружает данные в юниформу
     */
    public abstract void upload();
}
