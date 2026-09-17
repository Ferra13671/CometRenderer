package com.ferra13671.cometrenderer.plugins.glfw;

import com.ferra13671.cometrenderer.utils.GLVersion;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.apiguardian.api.API;

@API(status = API.Status.MAINTAINED, since = "3.0")
@Builder
@Getter
public class WindowHints {
    @Builder.Default
    private GLVersion glVersion = null;
    @Builder.Default
    private boolean debugContext = false;
    @Builder.Default
    @NonNull
    private GLProfile glProfile = GLProfile.Any;

    @Builder.Default
    private boolean resizable = true;
    @Builder.Default
    private boolean decorated = true;
    @Builder.Default
    private boolean autoIconify = true;
    @Builder.Default
    private boolean transparent = false;
    @Builder.Default
    private boolean focusOnShow = true;
}
