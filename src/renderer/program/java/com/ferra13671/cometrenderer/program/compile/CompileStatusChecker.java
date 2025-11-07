package com.ferra13671.cometrenderer.program.compile;

import com.mojang.blaze3d.opengl.GlStateManager;
import org.apache.commons.lang3.StringUtils;
import com.ferra13671.cometrenderer.global.GlobalCometCompiler;
import com.ferra13671.cometrenderer.program.shader.GlShader;
import com.ferra13671.cometrenderer.program.GlProgram;
import org.lwjgl.opengl.GL20;

/**
 * Класс, предназначенный для проверки статуса компиляции.
 *
 * @see GlobalCometCompiler
 */
//TODO сделать проверку компиляции в самом компилируемом объекте.
public class CompileStatusChecker {

    /**
     * Проверяет и возвращает статус компиляции шейдера.
     *
     * @param shaderId айди шейдера в OpenGL.
     * @return статус компиляции шейдера.
     *
     * @see GlShader
     */
    public static CompileResult checkShaderCompile(int shaderId) {
        CompileStatus status = CompileStatus.fromStatusId(GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS));
        String message = status == CompileStatus.FAILURE ? StringUtils.trim(GlStateManager.glGetShaderInfoLog(shaderId, 32768)) : "";
        return new CompileResult(status, message);
    }

    /**
     * Проверяет и возвращает статус компиляции программы.
     *
     * @param programId айди программы в OpenGL.
     * @return статус компиляции программы.
     *
     * @see GlProgram
     */
    public static CompileResult checkProgramCompile(int programId) {
        CompileStatus status = CompileStatus.fromStatusId(GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS));
        String message = status == CompileStatus.FAILURE ? StringUtils.trim(GL20.glGetProgramInfoLog(programId)) : "";
        return new CompileResult(status, message);
    }
}
