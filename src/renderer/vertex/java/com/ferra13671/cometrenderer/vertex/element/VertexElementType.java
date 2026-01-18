package com.ferra13671.cometrenderer.vertex.element;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.exceptions.impl.vertex.IllegalVertexElementStructureException;
import org.joml.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;

import java.awt.*;
import java.util.function.BiConsumer;

/**
 * Тип данных, передаваемых элементу.
 * Хотя OpenGL поддерживает только примитивные типы, но при помощи данного класса можно написать свои типы данных.
 *
 * @param name имя типа данных.
 * @param size размер типа данных в байтах. Размер должен быть > 0.
 * @param offset смещение в байтах между примитивными элементами в буффере. Значение должно быть <=size и > 0, а так же size должен быть кратен offset. Позволяет определять внутри элементами несколько примитивных элементов (Например несколько float чисел в одном элементе).
 * @param glId айди типа данных в OpenGL.
 * @param clazz класс типа данных.
 * @param uploadConsumer метод, загружающий тип данных по указанному адресу.
 * @param <T> тип данных.
 *
 * @see VertexElement
 * @see VertexFormat
 */
public record VertexElementType<T>(String name, int size, int offset, int glId, Class<T> clazz, BiConsumer<Long, T[]> uploadConsumer) {
    public static final VertexElementType<Float> FLOAT = builder(Float.class)
            .name("Float")
            .offset(4)
            .glId(GL11.GL_FLOAT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++)
                            MemoryUtil.memPutFloat(pointer + (4L * i), data[i]);
                    }
            )
            .build();
    public static final VertexElementType<Integer> INT = builder(Integer.class)
            .name("Int")
            .offset(4)
            .glId(GL11.GL_INT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++)
                            MemoryUtil.memPutInt(pointer + (4L * i), data[i]);
                    }
            )
            .build();
    public static final VertexElementType<Short> SHORT = builder(Short.class)
            .name("Short")
            .offset(2)
            .glId(GL11.GL_SHORT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++)
                            MemoryUtil.memPutShort(pointer + (2L * i), data[i]);
                    }
            )
            .build();
    public static final VertexElementType<Byte> BYTE = builder(Byte.class)
            .name("Byte")
            .offset(1)
            .glId(GL11.GL_BYTE)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++)
                            MemoryUtil.memPutByte(pointer + i, data[i]);
                    }
            )
            .build();
    public static final VertexElementType<Integer> UNSIGNED_INT = builder(Integer.class)
            .name("Unsigned int")
            .offset(4)
            .glId(GL11.GL_UNSIGNED_INT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++)
                            MemoryUtil.memPutInt(pointer + (4L * i), data[i]);
                    }
            )
            .build();
    public static final VertexElementType<Short> UNSIGNED_SHORT = builder(Short.class)
            .name("Unsigned short")
            .offset(2)
            .glId(GL11.GL_UNSIGNED_SHORT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++)
                            MemoryUtil.memPutShort(pointer + (2L * i), data[i]);
                    }
            )
            .build();
    public static final VertexElementType<Byte> UNSIGNED_BYTE = builder(Byte.class)
            .name("Unsigned byte")
            .offset(1)
            .glId(GL11.GL_UNSIGNED_BYTE)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++)
                            MemoryUtil.memPutByte(pointer + i, data[i]);
                    }
            )
            .build();
    public static final VertexElementType<Vector2i> VECTOR_2_INT = builder(Vector2i.class)
            .name("Vector2i")
            .size(8)
            .offset(4)
            .glId(GL11.GL_INT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            MemoryUtil.memPutInt(pointer + (4L * i), data[i].x);
                            MemoryUtil.memPutInt(pointer + (4L * i) + 4L, data[i].y);
                        }
                    }
            )
            .build();
    public static final VertexElementType<Vector2i> UNSIGNED_VECTOR_2_INT = builder(Vector2i.class)
            .name("Unsigned Vector2i")
            .size(8)
            .offset(4)
            .glId(GL11.GL_UNSIGNED_INT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            MemoryUtil.memPutInt(pointer + (4L * i), data[i].x);
                            MemoryUtil.memPutInt(pointer + (4L * i) + 4L, data[i].y);
                        }
                    }
            )
            .build();
    public static final VertexElementType<Vector2f> VECTOR_2_FLOAT = builder(Vector2f.class)
            .name("Vector2f")
            .size(8)
            .offset(4)
            .glId(GL11.GL_FLOAT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            MemoryUtil.memPutFloat(pointer + (4L * i), data[i].x);
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 4L, data[i].y);
                        }
                    }
            )
            .build();
    public static final VertexElementType<Vector3i> VECTOR_3_INT = builder(Vector3i.class)
            .name("Vector3i")
            .size(12)
            .offset(4)
            .glId(GL11.GL_INT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            MemoryUtil.memPutInt(pointer + (4L * i), data[i].x);
                            MemoryUtil.memPutInt(pointer + (4L * i) + 4L, data[i].y);
                            MemoryUtil.memPutInt(pointer + (4L * i) + 8L, data[i].z);
                        }
                    }
            )
            .build();
    public static final VertexElementType<Vector3i> UNSIGNED_VECTOR_3_INT = builder(Vector3i.class)
            .name("Unsigned Vector3i")
            .size(12)
            .offset(4)
            .glId(GL11.GL_UNSIGNED_INT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            MemoryUtil.memPutInt(pointer + (4L * i), data[i].x);
                            MemoryUtil.memPutInt(pointer + (4L * i) + 4L, data[i].y);
                            MemoryUtil.memPutInt(pointer + (4L * i) + 8L, data[i].z);
                        }
                    }
            )
            .build();
    public static final VertexElementType<Vector3f> VECTOR_3_FLOAT = builder(Vector3f.class)
            .name("Vector3f")
            .size(12)
            .offset(4)
            .glId(GL11.GL_FLOAT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            MemoryUtil.memPutFloat(pointer + (4L * i), data[i].x);
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 4L, data[i].y);
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 8L, data[i].z);
                        }
                    }
            )
            .build();
    public static final VertexElementType<Vector4i> VECTOR_4_INT = builder(Vector4i.class)
            .name("Vector4i")
            .size(16)
            .offset(4)
            .glId(GL11.GL_INT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            MemoryUtil.memPutInt(pointer + (4L * i), data[i].x);
                            MemoryUtil.memPutInt(pointer + (4L * i) + 4L, data[i].y);
                            MemoryUtil.memPutInt(pointer + (4L * i) + 8L, data[i].z);
                            MemoryUtil.memPutInt(pointer + (4L * i) + 12L, data[i].w);
                        }
                    }
            )
            .build();
    public static final VertexElementType<Vector4i> UNSIGNED_VECTOR_4_INT = builder(Vector4i.class)
            .name("Unsigned Vector4i")
            .size(16)
            .offset(4)
            .glId(GL11.GL_UNSIGNED_INT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            MemoryUtil.memPutInt(pointer + (4L * i), data[i].x);
                            MemoryUtil.memPutInt(pointer + (4L * i) + 4L, data[i].y);
                            MemoryUtil.memPutInt(pointer + (4L * i) + 8L, data[i].z);
                            MemoryUtil.memPutInt(pointer + (4L * i) + 12L, data[i].w);
                        }
                    }
            )
            .build();
    public static final VertexElementType<Vector4f> VECTOR_4_FLOAT = builder(Vector4f.class)
            .name("Vector4f")
            .size(16)
            .offset(4)
            .glId(GL11.GL_FLOAT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            MemoryUtil.memPutFloat(pointer + (4L * i), data[i].x);
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 4L, data[i].y);
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 8L, data[i].z);
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 12L, data[i].w);
                        }
                    }
            )
            .build();
    public static final VertexElementType<Color> COLOR_FLOAT = builder(Color.class)
            .name("Color float")
            .size(16)
            .offset(4)
            .glId(GL11.GL_FLOAT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            MemoryUtil.memPutFloat(pointer + (4L * i), data[i].getRed() / 255f);
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 4L, data[i].getGreen() / 255f);
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 8L, data[i].getBlue() / 255f);
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 12L, data[i].getAlpha() / 255f);
                        }
                    }
            )
            .build();
    public static final VertexElementType<Color> COLOR_INT = builder(Color.class)
            .name("Color int")
            .size(16)
            .offset(4)
            .glId(GL11.GL_INT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            MemoryUtil.memPutInt(pointer + (4L * i), data[i].getRed());
                            MemoryUtil.memPutInt(pointer + (4L * i) + 4L, data[i].getGreen());
                            MemoryUtil.memPutInt(pointer + (4L * i) + 8L, data[i].getBlue());
                            MemoryUtil.memPutInt(pointer + (4L * i) + 12L, data[i].getAlpha());
                        }
                    }
            )
            .build();

    //TODO change
    public void verify() {
        if (
                this.size <= 0
                || this.offset <= 0
                || (this.size % this.offset != 0)
                        || (this.offset > this.size)
        ) {
            String reason = "";

            if (this.size <= 0)
                reason = String.format("size(%s) must be > 0", this.size);
            else if (this.offset <= 0)
                reason = String.format("offset(%s) must be > 0", this.offset);
            else if (this.size % this.offset != 0)
                reason = String.format("size(%s) must be a multiple of offset(%s)", this.size, this.offset);
            else if (this.offset > this.size)
                reason = String.format("offset(%s) must be <= size(%s)", this.offset, this.size);

            CometRenderer.manageException(new IllegalVertexElementStructureException(this, reason));
        }
    }

    public static <T> VertexElementTypeBuilder<T> builder(Class<T> clazz) {
        return new VertexElementTypeBuilder<T>().clazz(clazz);
    }
}
