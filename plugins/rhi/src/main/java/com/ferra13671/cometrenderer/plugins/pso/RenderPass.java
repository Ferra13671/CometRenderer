package com.ferra13671.cometrenderer.plugins.pso;

import com.ferra13671.cometrenderer.buffer.framebuffer.Framebuffer;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.apiguardian.api.API;

@API(status = API.Status.MAINTAINED, since = "3.1")
@Builder
@Getter
public class RenderPass {
    @NonNull
    private final String name;
    @NonNull
    private final Framebuffer framebuffer;
    private final boolean clearColor;
    private final boolean clearDepth;
    private final boolean clearStencil;
    @NonNull
    private final PipelineStateObject pso;

    public void begin() {
        this.framebuffer.bind(true);

        if (this.clearColor)
            this.framebuffer.clearColor();
        if (this.clearDepth)
            this.framebuffer.clearDepth();
        if (this.clearStencil)
            this.framebuffer.clearStencil();

        this.pso.bind();
    }

    public void end() {}

    public void run(Runnable body) {
        begin();

        try {
            body.run();
        } finally {
            end();
        }
    }
}
