package com.ferra13671.cometrenderer.program.shader;

import org.lwjgl.opengl.GL20;

/*
 * Тип шейдера.
 * Всего есть 5 видов шейдеров, но нам будет достаточно 2 вида шейдера для компиляции программы:
 *   Vertex — вертексный шейдер, модифицирует входящие позиции. Так же может передавать другим шейдерам различную информацию к позициям.
 *   Fragment — фрагментный шейдер. Получает на входе различную информацию и позицию пикселя, а на выходе дает цвет пикселя.
 */
public enum ShaderType {
    Vertex(GL20.GL_VERTEX_SHADER),
    Fragment(GL20.GL_FRAGMENT_SHADER);

    private final int id;

    ShaderType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
