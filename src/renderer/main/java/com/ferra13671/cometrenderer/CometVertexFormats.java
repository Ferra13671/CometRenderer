package com.ferra13671.cometrenderer;

import com.ferra13671.cometrenderer.builders.VertexFormatBuilder;
import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;

/**
 * Самые примитивные форматы вершин, которые можно вообще придумать.
 */
public final class CometVertexFormats {

    public static final VertexFormat POSITION = VertexFormatBuilder.builder().build();

    public static final VertexFormat POSITION_COLOR = VertexFormatBuilder.builder()
            .element("Color", VertexElementType.FLOAT, 4)
            .build();

    public static final VertexFormat POSITION_TEXTURE = VertexFormatBuilder.builder()
            .element("Texture", VertexElementType.FLOAT, 2)
            .build();

    public static final VertexFormat POSITION_COLOR_TEXTURE = VertexFormatBuilder.builder()
            .element("Color", VertexElementType.FLOAT, 4)
            .element("Texture", VertexElementType.FLOAT, 2)
            .build();

    public static final VertexFormat POSITION_TEXTURE_COLOR = VertexFormatBuilder.builder()
            .element("Texture", VertexElementType.FLOAT, 2)
            .element("Color", VertexElementType.FLOAT, 4)
            .build();
}
