package com.ferra13671.cometrenderer.program.uniform;

import com.ferra13671.cometrenderer.program.GlProgram;
import com.ferra13671.cometrenderer.program.uniform.uniforms.*;
import com.ferra13671.cometrenderer.program.uniform.uniforms.sampler.SamplerUniform;
import org.apache.commons.lang3.function.TriFunction;

/*
 * Типы юниформ, которые на данный момент поддерживаются CometRenderer
 */
public record UniformType<T extends GlUniform>(Class<T> clazz, TriFunction<String, Integer, GlProgram, GlUniform> uniformCreator) {
    public static final UniformType<IntUniform> INT = new UniformType<>(IntUniform.class, IntUniform::new);
    public static final UniformType<FloatUniform> FLOAT = new UniformType<>(FloatUniform.class, FloatUniform::new);
    public static final UniformType<IntArrayUniform> INT_ARRAY = new UniformType<>(IntArrayUniform.class, IntArrayUniform::new);
    public static final UniformType<FloatArrayUniform> FLOAT_ARRAY = new UniformType<>(FloatArrayUniform.class, FloatArrayUniform::new);
    public static final UniformType<Vec2GlUniform> VEC2 = new UniformType<>(Vec2GlUniform.class, Vec2GlUniform::new);
    public static final UniformType<Vec3GlUniform> VEC3 = new UniformType<>(Vec3GlUniform.class, Vec3GlUniform::new);
    public static final UniformType<Vec4GlUniform> VEC4 = new UniformType<>(Vec4GlUniform.class, Vec4GlUniform::new);
    public static final UniformType<IntVec2GlUniform> IVEC2 = new UniformType<>(IntVec2GlUniform.class, IntVec2GlUniform::new);
    public static final UniformType<IntVec3GlUniform> IVEC3 = new UniformType<>(IntVec3GlUniform.class, IntVec3GlUniform::new);
    public static final UniformType<IntVec4GlUniform> IVEC4 = new UniformType<>(IntVec4GlUniform.class, IntVec4GlUniform::new);
    public static final UniformType<Matrix4fGlUniform> MATRIX = new UniformType<>(Matrix4fGlUniform.class, Matrix4fGlUniform::new);
    public static final UniformType<BufferUniform> BUFFER = new UniformType<>(BufferUniform.class, BufferUniform::new);
    public static final UniformType<SamplerUniform> SAMPLER = new UniformType<>(SamplerUniform.class, SamplerUniform::new);
}
