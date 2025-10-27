package com.ferra13671.cometrenderer.global;

import com.ferra13671.cometrenderer.builders.GlUniformSchema;

import java.util.List;

/*
 * Контент шейдера, который хранит в себе как и обычный контент, так и юниформы, добавленные шейдерными библиотеками
 */
public record ShaderContent(String content, List<GlUniformSchema<?>> uniforms) {
}
