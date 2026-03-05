package com.ferra13671.cometrenderer.sampler;

import org.apiguardian.api.API;

@API(status = API.Status.MAINTAINED, since = "2.1")
public interface ISamplerManger {

    ISampler createSampler();

    void bindSampler(int uniformSamplerId, ISampler sampler);
}
