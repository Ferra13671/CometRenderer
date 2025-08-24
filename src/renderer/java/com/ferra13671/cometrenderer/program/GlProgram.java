package com.ferra13671.cometrenderer.program;

import com.ferra13671.cometrenderer.exceptions.CompileProgramException;
import com.ferra13671.cometrenderer.exceptions.IllegalShaderFormatException;
import com.ferra13671.cometrenderer.program.compile.CompileResult;
import com.ferra13671.cometrenderer.program.compile.CompileStatusChecker;
import com.ferra13671.cometrenderer.program.schema.GlUniformSchema;
import com.ferra13671.cometrenderer.program.shader.Shader;
import com.ferra13671.cometrenderer.program.shader.ShaderType;
import com.ferra13671.cometrenderer.program.uniform.GlUniform;
import org.lwjgl.opengl.GL20;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GlProgram {
    public static GlProgram ACTIVE_PROGRAM = null;

    private final String name;
    private final int id;
    private final List<GlUniform> uniforms = new ArrayList<>();
    private final HashMap<String, GlUniform> uniformsByName = new HashMap<>();
    //Количество семплеров (Юниформ с типом SAMPLER)
    private int samplersAmount = 0;
    //Количество забинженных буфферов (Юниформ с типом BUFFER)
    private int buffersBindingsAmount = 0;

    private GlProgram(String name, int id, List<GlUniformSchema> uniforms) {
        this.name = name;
        this.id = id;

        for (GlUniformSchema glUniformSchema : uniforms) {
            GlUniform uniform = glUniformSchema.uniformType().uniformCreator.apply(glUniformSchema.name(), glUniformSchema.getIdFromProgram(this.id), this);
            this.uniforms.add(uniform);
            this.uniformsByName.put(glUniformSchema.name(), uniform);
        }
    }

    /*
     * Привязывает к OpenGl программу
     */
    public void bind() {
        GL20.glUseProgram(getId());

        for (GlUniform glUniform : uniforms)
            glUniform.upload();

        ACTIVE_PROGRAM = this;
    }

    /*
     * Отвязывает с OpenGl программу
     */
    public void unBind() {
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
     * Возвращает юниформу по её имени и типу
     */
    public <T extends GlUniform> T getUniform(String name, Class<T> clazz) {
        return (T) uniformsByName.get(name);
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

    /*
     * Компилирует программу
     */
    public static GlProgram compile(String name, Shader vertexShader, Shader fragmentShader, List<GlUniformSchema> uniforms) {
        if (vertexShader.getShaderType() != ShaderType.Vertex)
            throw new IllegalShaderFormatException(String.format("'%s' is not a vertex shader.", vertexShader.getName()));
        if (fragmentShader.getShaderType() != ShaderType.Fragment)
            throw new IllegalShaderFormatException(String.format("'%s' is not a fragment shader.", fragmentShader.getName()));

        int programId = GL20.glCreateProgram();
        GL20.glAttachShader(programId, vertexShader.getId());
        GL20.glAttachShader(programId, fragmentShader.getId());
        GL20.glLinkProgram(programId);

        CompileResult compileResult = CompileStatusChecker.checkProgramCompile(programId);
        if (compileResult.isFailure())
            throw new CompileProgramException(String.format("Error compiling program '%s', reason: %s", name, compileResult.message()));

        return new GlProgram(name, programId, uniforms);
    }
}
