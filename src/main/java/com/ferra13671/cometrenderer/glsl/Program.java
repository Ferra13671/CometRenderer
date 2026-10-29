package com.ferra13671.cometrenderer.glsl;

import com.ferra13671.cometrenderer.glsl.uniform.GLUniform;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import com.ferra13671.cometrenderer.glsl.uniform.uniforms.SamplerUniform;
import com.ferra13671.cometrenderer.utils.Bindable;
import org.apiguardian.api.API;

import java.io.Closeable;
import java.util.function.Consumer;

@API(status = API.Status.MAINTAINED, since = "3.1")
public interface Program extends Bindable, Closeable {

    @API(status = API.Status.STABLE, since = "1.1")
    String getName();

    @API(status = API.Status.STABLE, since = "1.1")
    int getId();

    @API(status = API.Status.STABLE, since = "1.6")
    <T extends GLUniform> T getUniform(String name, UniformType<T> type);

    @API(status = API.Status.STABLE, since = "1.6")
    <T extends GLUniform> T getUniformNullable(String name, UniformType<T> type);

    @API(status = API.Status.MAINTAINED, since = "1.8.3")
    <T extends GLUniform> void consumeIfUniformPresent(String name, UniformType<T> type, Consumer<T> consumer);

    @API(status = API.Status.MAINTAINED, since = "1.5")
    SamplerUniform getSampler(int samplerId);

    @API(status = API.Status.MAINTAINED, since = "1.8.3")
    SamplerUniform getSamplerNullable(int samplerId);

    @API(status = API.Status.MAINTAINED, since = "1.8.3")
    void consumerIfSamplerPresent(int samplerId, Consumer<SamplerUniform> consumer);
}
