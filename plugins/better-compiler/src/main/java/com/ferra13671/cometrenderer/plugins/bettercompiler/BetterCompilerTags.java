package com.ferra13671.cometrenderer.plugins.bettercompiler;

import com.ferra13671.cometrenderer.utils.GLVersion;
import com.ferra13671.cometrenderer.utils.tag.Tag;
import lombok.experimental.UtilityClass;
import org.apiguardian.api.API;

@API(status = API.Status.MAINTAINED, since = "2.5")
@UtilityClass
public class BetterCompilerTags {
    public final Tag<GLVersion> GLSL_VERSION = new Tag<>("glsl-version");
    public final Tag<BetterCompilerProgramInfo> PROGRAM_INFO = new Tag<>("better-compiler-program-info");
}
