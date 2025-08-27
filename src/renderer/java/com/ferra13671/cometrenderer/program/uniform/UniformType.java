package com.ferra13671.cometrenderer.program.uniform;

import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.uniform.uniforms.*;
import com.ferra13671.cometrenderer.program.uniform.uniforms.sampler.SamplerUniform;
import org.apache.commons.lang3.function.TriFunction;

/*
 * Типы юниформ, которые на данный момент поддерживаются CometRenderer
 */
public enum UniformType {
    INT(IntUniform::new),
    FLOAT(FloatUniform::new),
    VEC2(Vec2GlUniform::new),
    VEC3(Vec3GlUniform::new),
    VEC4(Vec4GlUniform::new),
    MATRIX(Matrix4fGlUniform::new),
    BUFFER(BufferUniform::new),
    SAMPLER(SamplerUniform::new);

    public final TriFunction<String, Integer, GlProgram, GlUniform> uniformCreator;


    UniformType(TriFunction<String, Integer, GlProgram, GlUniform> uniformCreator) {
        this.uniformCreator = uniformCreator;
    }
}
