package com.ferra13671.cometrenderer.program.schema;

import java.util.List;

/*
 * Схема, используемая при компиляции программы.
 */
public record GlProgramSchema(String name, ShaderSchema vertexShader, ShaderSchema fragmentShader, List<GlUniformSchema> uniforms) {}
