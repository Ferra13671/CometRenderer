package com.ferra13671.cometrenderer.vertex.format;

import com.ferra13671.cometrenderer.vertex.element.VertexElement;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;

import java.util.ArrayList;
import java.util.List;

public final class VertexFormatBuilder {
    private final List<VertexElement> vertexElements = new ArrayList<>();
    private final List<String> elementNames = new ArrayList<>();

    private VertexFormatBuilder() {}

    public VertexFormatBuilder element(String name, VertexElementType<?> type, int count) {
        int id = vertexElements.size();
        elementNames.add(name);
        vertexElements.add(new VertexElement(id, count, type));

        return this;
    }

    public VertexFormat build() {
        return new VertexFormat(vertexElements, elementNames);
    }

    public static VertexFormatBuilder builder() {
        return new VertexFormatBuilder().element("Position", VertexElementType.FLOAT, 3);
    }
}
