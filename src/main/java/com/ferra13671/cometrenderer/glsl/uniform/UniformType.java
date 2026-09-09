package com.ferra13671.cometrenderer.glsl.uniform;

import com.ferra13671.cometrenderer.glsl.GLProgram;
import com.ferra13671.cometrenderer.glsl.uniform.uniforms.*;
import com.ferra13671.cometrenderer.glsl.uniform.uniforms.BufferUniform;
import com.ferra13671.cometrenderer.glsl.uniform.uniforms.SamplerUniform;
import org.apiguardian.api.API;

import java.util.function.BiFunction;

/**
 * Объект, представляющий собой тип униформы.
 * Какой тип имеет униформа, такой тип данных она и будет принимать.
 *
 * @param clazz класс униформы.
 * @param uniformCreator функция, создающая новую униформу из входных данных (имя, локация, программа ({@link GLProgram}))
 * @param <T> униформа.
 *
 * @see GLUniform
 */
@API(status = API.Status.STABLE, since = "1.1")
public record UniformType<T extends GLUniform>(Class<T> clazz, BiFunction<String, Integer, GLUniform> uniformCreator) {
    public static final UniformType<IntUniform> INT = new UniformType<>(IntUniform.class, IntUniform::new);
    public static final UniformType<FloatUniform> FLOAT = new UniformType<>(FloatUniform.class, FloatUniform::new);
    public static final UniformType<IntArrayUniform> INT_ARRAY = new UniformType<>(IntArrayUniform.class, IntArrayUniform::new);
    public static final UniformType<FloatArrayUniform> FLOAT_ARRAY = new UniformType<>(FloatArrayUniform.class, FloatArrayUniform::new);
    public static final UniformType<Vec2GLUniform> VEC2 = new UniformType<>(Vec2GLUniform.class, Vec2GLUniform::new);
    public static final UniformType<Vec3GLUniform> VEC3 = new UniformType<>(Vec3GLUniform.class, Vec3GLUniform::new);
    public static final UniformType<Vec4GLUniform> VEC4 = new UniformType<>(Vec4GLUniform.class, Vec4GLUniform::new);
    public static final UniformType<IntVec2GLUniform> IVEC2 = new UniformType<>(IntVec2GLUniform.class, IntVec2GLUniform::new);
    public static final UniformType<IntVec3GLUniform> IVEC3 = new UniformType<>(IntVec3GLUniform.class, IntVec3GLUniform::new);
    public static final UniformType<IntVec4GLUniform> IVEC4 = new UniformType<>(IntVec4GLUniform.class, IntVec4GLUniform::new);
    public static final UniformType<Matrix4FGLUniform> MATRIX4 = new UniformType<>(Matrix4FGLUniform.class, Matrix4FGLUniform::new);
    public static final UniformType<BufferUniform> BUFFER = new UniformType<>(BufferUniform.class, BufferUniform::new);
    public static final UniformType<SamplerUniform> SAMPLER = new UniformType<>(SamplerUniform.class, SamplerUniform::new);
}
