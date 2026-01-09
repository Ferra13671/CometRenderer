package com.ferra13671.cometrenderer;

import com.ferra13671.cometrenderer.utils.Setting;

public class Config {

    public final Setting<Boolean> CHECK_OPENGL_VERSION = new Setting<>(true);
    public final Setting<Boolean> COMPARE_CURRENT_AND_SHADER_OPENGL_VERSIONS = new Setting<>(true);
    public final Setting<Boolean> USE_ARB_ATTRIB_BINDING_IF_SUPPORT = new Setting<>(true);
    public final Setting<Boolean> DONT_THROW_EXCEPTIONS = new Setting<>(false);
}
