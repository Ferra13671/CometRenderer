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
 * @param byteSize размер типа данных в байтах. Размер должен быть > 0.
 * @param offset смещение в байтах между примитивными элементами в буффере. Значение должно быть <=byteSize и > 0, а так же byteSize должен быть кратен offset. Позволяет определять внутри элементами несколько примитивных элементов (Например несколько float чисел в одном элементе).
 * @param typeName имя типа данных.
 * @param glId айди типа данных в OpenGL.
 * @param clazz класс типа данных.
 * @param uploadConsumer метод, загружающий тип данных по указанному адресу.
 * @param <T> тип данных.
 *
 * @see VertexElement
 * @see VertexFormat
 */
public record VertexElementType<T>(int byteSize, int offset, String typeName, int glId, Class<T> clazz, BiConsumer<Long, T[]> uploadConsumer) {
    public static final VertexElementType<Float> FLOAT = new VertexElementType<>(
            4,
            4,
            "Float",
            GL11.GL_FLOAT,
            Float.class,
            (pointer, data) -> {
                for (int i = 0; i < data.length; i++)
                    MemoryUtil.memPutFloat(pointer + (4L * i), data[i]);
            }
    );
    public static final VertexElementType<Byte> UNSIGNED_BYTE = new VertexElementType<>(
            1,
            1,
            "Unsigned Byte",
            GL11.GL_UNSIGNED_BYTE,
            Byte.class,
            (pointer, data) -> {
                for (int i = 0; i < data.length; i++)
                    MemoryUtil.memPutByte(pointer + i, data[i]);
            }
    );
    public static final VertexElementType<Byte> BYTE = new VertexElementType<>(
            1,
            1,
            "Byte",
            GL11.GL_BYTE,
            Byte.class,
            (pointer, data) -> {
                for (int i = 0; i < data.length; i++)
                    MemoryUtil.memPutByte(pointer + i, data[i]);
            }
    );
    public static final VertexElementType<Short> UNSIGNED_SHORT = new VertexElementType<>(
            2,
            2,
            "Unsigned Short",
            GL11.GL_SHORT,
            Short.class,
            (pointer, data) -> {
                for (int i = 0; i < data.length; i++)
                    MemoryUtil.memPutShort(pointer + (2L * i), data[i]);
            }
    );
    public static final VertexElementType<Short> SHORT = new VertexElementType<>(
            2,
            2,
            "Short",
            GL11.GL_UNSIGNED_SHORT,
            Short.class,
            (pointer, data) -> {
                for (int i = 0; i < data.length; i++)
                    MemoryUtil.memPutShort(pointer + (2L * i), data[i]);
            }
    );
    public static final VertexElementType<Integer> UNSIGNED_INT = new VertexElementType<>(
            4,
            4,
            "Unsigned Int",
            GL11.GL_UNSIGNED_INT,
            Integer.class,
            (pointer, data) -> {
                for (int i = 0; i < data.length; i++)
                    MemoryUtil.memPutInt(pointer + (4L * i), data[i]);
            }
    );
    public static final VertexElementType<Integer> INT = new VertexElementType<>(
            4,
            4,
            "Int",
            GL11.GL_INT,
            Integer.class,
            (pointer, data) -> {
                for (int i = 0; i < data.length; i++)
                    MemoryUtil.memPutInt(pointer + (4L * i), data[i]);
            }
    );

    public void verify() {
        if (
                this.byteSize <= 0
                || this.offset <= 0
                || (this.byteSize % this.offset != 0)
                        || (this.offset > this.byteSize)
        )
            CometRenderer.manageException(new IllegalVertexElementStructureException(this));
    }
}
