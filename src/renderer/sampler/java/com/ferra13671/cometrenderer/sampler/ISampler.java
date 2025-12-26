package com.ferra13671.cometrenderer.sampler;

import com.ferra13671.gltextureutils.TextureFiltering;
import com.ferra13671.gltextureutils.TextureWrapping;

public interface ISampler {

    void setFiltering(TextureFiltering filtering);

    void setWrapping(TextureWrapping wrapping);
}
