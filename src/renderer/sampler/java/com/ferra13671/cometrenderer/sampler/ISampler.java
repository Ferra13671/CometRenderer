package com.ferra13671.cometrenderer.sampler;

import com.ferra13671.gltextureutils.TextureFiltering;
import com.ferra13671.gltextureutils.TextureWrapping;
import org.apiguardian.api.API;

@API(status = API.Status.MAINTAINED, since = "2.1")
public interface ISampler {

    void setFiltering(TextureFiltering filtering);

    void setWrapping(TextureWrapping wrapping);
}
