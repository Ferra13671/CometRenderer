package com.ferra13671.cometrenderer;

import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;

/**
 * Самые примитивные форматы вершин, которые можно вообще придумать.
 *
 * @see VertexFormat
 */
public final class CometVertexFormats {

    public static final VertexFormat POSITION = VertexFormat.builder().build();

    public static final VertexFormat POSITION_COLOR = VertexFormat.builder()
            .element("Color", VertexElementType.FLOAT, 4)
            .build();

    public static final VertexFormat POSITION_TEXTURE = VertexFormat.builder()
            .element("Texture", VertexElementType.FLOAT, 2)
            .build();

    public static final VertexFormat POSITION_COLOR_TEXTURE = VertexFormat.builder()
            .element("Color", VertexElementType.FLOAT, 4)
            .element("Texture", VertexElementType.FLOAT, 2)
            .build();

    public static final VertexFormat POSITION_TEXTURE_COLOR = VertexFormat.builder()
            .element("Texture", VertexElementType.FLOAT, 2)
            .element("Color", VertexElementType.FLOAT, 4)
            .build();
}
