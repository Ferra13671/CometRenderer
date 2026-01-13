package com.ferra13671.cometrenderer.vertex.element;

import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.exceptions.impl.vertex.IllegalVertexElementStructureException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;

import java.util.function.BiConsumer;

/**
 * Тип данных, передаваемых элементу.
 * Хотя OpenGL поддерживает только примитивные типы, но при помощи данного класса можно написать свои типы данных.
 *
 * @param name имя типа данных.
 * @param elementSize размер элемента типа данных в байтах. Размер должен быть > 0.
 * @param offset смещение в байтах между примитивными элементами в буффере. Значение должно быть <=elementSize и > 0, а так же elementSize должен быть кратен offset. Позволяет определять внутри элементами несколько примитивных элементов (Например несколько float чисел в одном элементе).
 * @param glId айди типа данных в OpenGL.
 * @param clazz класс типа данных.
 * @param uploadConsumer метод, загружающий тип данных по указанному адресу.
 * @param <T> тип данных.
 *
 * @see VertexElement
 * @see VertexFormat
 */
//TODO
// MoreTypes
public record VertexElementType<T>(String name, int elementSize, int offset, int glId, Class<T> clazz, BiConsumer<Long, T[]> uploadConsumer) {
    public static final VertexElementType<Float> FLOAT = builder(Float.class)
            .name("Float")
            .elementSize(4)
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
            .elementSize(4)
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
            .elementSize(2)
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
            .elementSize(1)
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
            .elementSize(4)
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
            .elementSize(2)
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
            .elementSize(1)
            .glId(GL11.GL_UNSIGNED_BYTE)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++)
                            MemoryUtil.memPutByte(pointer + i, data[i]);
                    }
            )
            .build();

    public void verify() {
        if (
                this.elementSize <= 0
                || this.offset <= 0
                || (this.elementSize % this.offset != 0)
                        || (this.offset > this.elementSize)
        ) {
            String reason = "";

            if (this.elementSize <= 0)
                reason = String.format("elementSize(%s) must be > 0", this.elementSize);
            else if (this.offset <= 0)
                reason = String.format("offset(%s) must be > 0", this.offset);
            else if (this.elementSize % this.offset != 0)
                reason = String.format("elementSize(%s) must be a multiple of offset(%s)", this.elementSize, this.offset);
            else if (this.offset > this.elementSize)
                reason = String.format("offset(%s) must be <= elementSize(%s)", this.offset, this.elementSize);

            CometRenderer.manageException(new IllegalVertexElementStructureException(this, reason));
        }
    }

    public static <T> VertexElementTypeBuilder<T> builder(Class<T> clazz) {
        return new VertexElementTypeBuilder<T>().clazz(clazz);
    }
}
