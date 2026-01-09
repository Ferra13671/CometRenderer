package com.ferra13671.cometrenderer.plugins.minecraft;

import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import lombok.experimental.UtilityClass;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

@UtilityClass
public class CustomVertexElementTypes {

    public VertexElementType<RenderColor> RENDER_COLOR = new VertexElementType<>(
            16,
            4,
            "Render Color",
            GL11.GL_FLOAT,
            RenderColor.class,
            (pointer, data) -> {
                for (int i = 0; i < data.length; i++) {
                    float[] color = data[i].getColor();
                    long ptr = pointer + (16L * i);

                    MemoryUtil.memPutFloat(ptr, color[0]);
                    MemoryUtil.memPutFloat(ptr + 4L, color[1]);
                    MemoryUtil.memPutFloat(ptr + 8L, color[2]);
                    MemoryUtil.memPutFloat(ptr + 12L, color[3]);
                }
            }
    );
}
