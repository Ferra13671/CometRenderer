package com.ferra13671.cometrenderer.program.shader;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL43;

/*
 * Тип шейдера.
 * Всего есть 5 видов шейдеров, но нам будет достаточно 2 вида шейдера для компиляции программы:
 *   Vertex — вертексный шейдер, модифицирует входящие позиции. Так же может передавать другим шейдерам различную информацию к позициям.
 *   Fragment — фрагментный шейдер. Получает на входе различную информацию и позицию пикселя, а на выходе дает цвет пикселя.
 */
public enum ShaderType {
    Vertex(GL20.GL_VERTEX_SHADER),
    Fragment(GL20.GL_FRAGMENT_SHADER),
    Geometry(GL32.GL_GEOMETRY_SHADER),
    TessellateEvaluation(GL40.GL_TESS_EVALUATION_SHADER),
    TessellateControl(GL40.GL_TESS_CONTROL_SHADER),
    Compute(GL43.GL_COMPUTE_SHADER);

    public final int glId;

    ShaderType(int glId) {
        this.glId = glId;
    }
}
