package com.ferra13671.cometrenderer.vertex;

import com.ferra13671.cometrenderer.utils.Builder;
import org.apiguardian.api.API;

import java.util.function.Function;

@API(status = API.Status.MAINTAINED, since = "2.3")
public class DrawModeBuilder extends Builder<DrawMode> {
    private Integer glId;
    private boolean useIndexBuffer = false;
    private IndexBufferGenerator indexBufferGenerator = null;
    private Function<Integer, Integer> indexCountFunction = v -> 0;

    public DrawModeBuilder() {
        super("draw mode");
    }

    public DrawModeBuilder glId(Integer glId) {
        this.glId = glId;

        return this;
    }

    public DrawModeBuilder indexGeneration(IndexBufferGenerator indexBufferGenerator, Function<Integer, Integer> indexCountFunction) {
        this.indexBufferGenerator = indexBufferGenerator;
        if (indexCountFunction == null)
            indexCountFunction = v -> 0;

        this.indexCountFunction = indexCountFunction;
        this.useIndexBuffer = indexBufferGenerator != null;

        return this;
    }

    @Override
    public DrawMode build() {
        assertNotNull(this.glId, "glId");

        if (this.useIndexBuffer)
            assertNotNull(this.indexBufferGenerator, "index buffer generator");

        return new DrawMode(
                this.glId,
                this.useIndexBuffer,
                this.indexBufferGenerator,
                this.indexCountFunction
        );
    }
}
