package com.ferra13671.cometrenderer.vertex;

import com.ferra13671.cometrenderer.utils.Builder;

import java.util.function.Function;

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

    public DrawModeBuilder useIndexBuffer(boolean useIndexBuffer) {
        this.useIndexBuffer = useIndexBuffer;

        return this;
    }

    public DrawModeBuilder indexBufferGenerator(IndexBufferGenerator indexBufferGenerator) {
        this.indexBufferGenerator = indexBufferGenerator;

        return this;
    }

    public DrawModeBuilder indexCountFunction(Function<Integer, Integer> indexCountFunction) {
        if (indexCountFunction == null)
            indexCountFunction = v -> 0;

        this.indexCountFunction = indexCountFunction;

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
