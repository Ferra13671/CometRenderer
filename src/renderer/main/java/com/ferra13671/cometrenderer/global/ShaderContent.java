package com.ferra13671.cometrenderer.global;

import com.ferra13671.cometrenderer.builders.GlUniformSchema;

import java.util.List;

/**
 * Контент шейдера, хранящий как и обычный контент, так и униформы, добавленные шейдерными библиотеками.
 * Создаётся и используется только после внедрения шейдерных библиотек.
 *
 * @deprecated В будущем должен быть убран.
 *
 * @param content контент шейдера.
 * @param uniforms униформы, добавленные шейдерными библиотеками.
 */
//TODO убрать
@Deprecated(forRemoval = true)
public record ShaderContent(String content, List<GlUniformSchema<?>> uniforms) {
}
