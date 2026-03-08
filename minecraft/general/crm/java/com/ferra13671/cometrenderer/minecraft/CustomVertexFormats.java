package com.ferra13671.cometrenderer.minecraft;

import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CustomVertexFormats {

    public VertexFormat POSITION_COLOR = VertexFormat.builder()
            .element("Color", CustomVertexElementTypes.RENDER_COLOR, 1)
            .build();

    public VertexFormat POSITION_TEXTURE_COLOR = CustomVertexFormats.POSITION_COLOR.toBuilder()
            .element(1, "Texture", VertexElementType.FLOAT, 2)
            .build();

    public VertexFormat ROUNDED_RECT = CustomVertexFormats.POSITION_COLOR.toBuilder()
            .element("Rect Position", VertexElementType.FLOAT, 2)
            .element("Half Size", VertexElementType.FLOAT, 2)
            .element("Radius", VertexElementType.FLOAT, 1)
            .build();

    public VertexFormat ROUNDED_TEXTURE = CustomVertexFormats.ROUNDED_RECT.toBuilder()
            .element(1, "Texture", VertexElementType.FLOAT, 2)
            .build();
}
