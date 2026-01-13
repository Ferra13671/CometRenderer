package com.ferra13671.cometrenderer;

import com.ferra13671.cometrenderer.utils.setting.NumberSetting;
import com.ferra13671.cometrenderer.utils.setting.Setting;

public class Config {

    public final Setting<Boolean> CHECK_OPENGL_VERSION = new Setting<>(true);
    public final Setting<Boolean> COMPARE_CURRENT_AND_SHADER_OPENGL_VERSIONS = new Setting<>(true);
    public final Setting<Boolean> USE_ARB_ATTRIB_BINDING_IF_SUPPORT = new Setting<>(true);
    public final Setting<Boolean> DONT_THROW_EXCEPTIONS = new Setting<>(false);
    public final NumberSetting<Integer> DEFAULT_MESH_ALLOCATOR_SIZE = new NumberSetting<>(786432, 0, Integer.MAX_VALUE);
}
