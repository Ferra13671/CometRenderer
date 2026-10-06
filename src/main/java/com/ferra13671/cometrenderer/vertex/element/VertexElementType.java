package com.ferra13671.cometrenderer.vertex.element;

import com.ferra13671.cometrenderer.ErrorHandlers;
import lombok.Builder;
import org.apiguardian.api.API;
import org.joml.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;
import com.ferra13671.cometrenderer.vertex.format.VertexFormat;

import java.awt.*;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

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
@API(status = API.Status.MAINTAINED, since = "1.4")
@Builder
public record VertexElementType<T>(String name, int size, int offset, int glId, Class<T> clazz, BiConsumer<Long, T[]> uploadConsumer) {
    public static final VertexElementType<Float> FLOAT = builder(Float.class)
            .name("Float")
            .size(4)
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
            .size(4)
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
            .size(2)
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
            .size(1)
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
            .size(4)
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
            .size(2)
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
            .size(1)
            .offset(1)
            .glId(GL11.GL_UNSIGNED_BYTE)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++)
                            MemoryUtil.memPutByte(pointer + i, data[i]);
                    }
            )
            .build();
    public static final VertexElementType<Vector2ic> VECTOR_2_INT = builder(Vector2ic.class)
            .name("Vector2i")
            .size(8)
            .offset(4)
            .glId(GL11.GL_INT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            MemoryUtil.memPutInt(pointer + (4L * i), data[i].x());
                            MemoryUtil.memPutInt(pointer + (4L * i) + 4L, data[i].y());
                        }
                    }
            )
            .build();
    public static final VertexElementType<Vector2ic> UNSIGNED_VECTOR_2_INT = builder(Vector2ic.class)
            .name("Unsigned Vector2i")
            .size(8)
            .offset(4)
            .glId(GL11.GL_UNSIGNED_INT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            MemoryUtil.memPutInt(pointer + (4L * i), data[i].x());
                            MemoryUtil.memPutInt(pointer + (4L * i) + 4L, data[i].y());
                        }
                    }
            )
            .build();
    public static final VertexElementType<Vector2fc> VECTOR_2_FLOAT = builder(Vector2fc.class)
            .name("Vector2f")
            .size(8)
            .offset(4)
            .glId(GL11.GL_FLOAT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            MemoryUtil.memPutFloat(pointer + (4L * i), data[i].x());
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 4L, data[i].y());
                        }
                    }
            )
            .build();
    public static final VertexElementType<Vector3ic> VECTOR_3_INT = builder(Vector3ic.class)
            .name("Vector3i")
            .size(12)
            .offset(4)
            .glId(GL11.GL_INT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            MemoryUtil.memPutInt(pointer + (4L * i), data[i].x());
                            MemoryUtil.memPutInt(pointer + (4L * i) + 4L, data[i].y());
                            MemoryUtil.memPutInt(pointer + (4L * i) + 8L, data[i].z());
                        }
                    }
            )
            .build();
    public static final VertexElementType<Vector3ic> UNSIGNED_VECTOR_3_INT = builder(Vector3ic.class)
            .name("Unsigned Vector3i")
            .size(12)
            .offset(4)
            .glId(GL11.GL_UNSIGNED_INT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            MemoryUtil.memPutInt(pointer + (4L * i), data[i].x());
                            MemoryUtil.memPutInt(pointer + (4L * i) + 4L, data[i].y());
                            MemoryUtil.memPutInt(pointer + (4L * i) + 8L, data[i].z());
                        }
                    }
            )
            .build();
    public static final VertexElementType<Vector3fc> VECTOR_3_FLOAT = builder(Vector3fc.class)
            .name("Vector3f")
            .size(12)
            .offset(4)
            .glId(GL11.GL_FLOAT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            MemoryUtil.memPutFloat(pointer + (4L * i), data[i].x());
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 4L, data[i].y());
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 8L, data[i].z());
                        }
                    }
            )
            .build();
    public static final VertexElementType<Vector4ic> VECTOR_4_INT = builder(Vector4ic.class)
            .name("Vector4i")
            .size(16)
            .offset(4)
            .glId(GL11.GL_INT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            MemoryUtil.memPutInt(pointer + (4L * i), data[i].x());
                            MemoryUtil.memPutInt(pointer + (4L * i) + 4L, data[i].y());
                            MemoryUtil.memPutInt(pointer + (4L * i) + 8L, data[i].z());
                            MemoryUtil.memPutInt(pointer + (4L * i) + 12L, data[i].w());
                        }
                    }
            )
            .build();
    public static final VertexElementType<Vector4ic> UNSIGNED_VECTOR_4_INT = builder(Vector4ic.class)
            .name("Unsigned Vector4i")
            .size(16)
            .offset(4)
            .glId(GL11.GL_UNSIGNED_INT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            MemoryUtil.memPutInt(pointer + (4L * i), data[i].x());
                            MemoryUtil.memPutInt(pointer + (4L * i) + 4L, data[i].y());
                            MemoryUtil.memPutInt(pointer + (4L * i) + 8L, data[i].z());
                            MemoryUtil.memPutInt(pointer + (4L * i) + 12L, data[i].w());
                        }
                    }
            )
            .build();
    public static final VertexElementType<Vector4fc> VECTOR_4_FLOAT = builder(Vector4fc.class)
            .name("Vector4f")
            .size(16)
            .offset(4)
            .glId(GL11.GL_FLOAT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            MemoryUtil.memPutFloat(pointer + (4L * i), data[i].x());
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 4L, data[i].y());
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 8L, data[i].z());
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 12L, data[i].w());
                        }
                    }
            )
            .build();
    @API(status = API.Status.MAINTAINED, since = "3.1")
    public static final VertexElementType<Matrix3fc> MATRIX_3_FLOAT = builder(Matrix3fc.class)
            .name("Matrix3f")
            .size(36)
            .offset(4)
            .glId(GL11.GL_FLOAT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            MemoryUtil.memPutFloat(pointer + (4L * i), data[i].m00());
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 4L, data[i].m01());
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 8L, data[i].m02());
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 12L, data[i].m10());
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 16L, data[i].m11());
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 20L, data[i].m12());
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 24L, data[i].m20());
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 28L, data[i].m21());
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 32L, data[i].m22());
                        }
                    }
            )
            .build();
    @API(status = API.Status.MAINTAINED, since = "3.1")
    public static final VertexElementType<Matrix4fc> MATRIX_4_FLOAT = builder(Matrix4fc.class)
            .name("Matrix3f")
            .size(36)
            .offset(4)
            .glId(GL11.GL_FLOAT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            MemoryUtil.memPutFloat(pointer + (4L * i), data[i].m00());
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 4L, data[i].m01());
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 8L, data[i].m02());
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 12L, data[i].m03());
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 16L, data[i].m10());
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 20L, data[i].m11());
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 24L, data[i].m12());
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 28L, data[i].m13());
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 32L, data[i].m20());
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 36L, data[i].m21());
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 40L, data[i].m22());
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 44L, data[i].m23());
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 48L, data[i].m30());
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 52L, data[i].m31());
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 56L, data[i].m32());
                            MemoryUtil.memPutFloat(pointer + (4L * i) + 60L, data[i].m33());
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

    public void verify() {
        for (ElementTypeVerifier verifier : ElementTypeVerifier.verifiers)
            if (!verifier.verifyFunction.apply(this))
                ErrorHandlers.onIllegalVertexElementStructure(this, verifier.messageFunction.apply(this.size, this.offset));
    }

    public static <T> VertexElementTypeBuilder<T> builder(Class<T> clazz) {
        return new VertexElementTypeBuilder<T>().clazz(clazz);
    }

    private record ElementTypeVerifier(Function<VertexElementType<?>, Boolean> verifyFunction, BiFunction<Integer, Integer, String> messageFunction) {
        static final List<ElementTypeVerifier> verifiers = List.of(
                new ElementTypeVerifier(
                        type -> type.size > 0,
                        (size, offset) -> String.format("size(%s) must be > 0", size)
                ),
                new ElementTypeVerifier(
                        type -> type.offset > 0,
                        (size, offset) -> String.format("offset(%s) must be > 0", offset)
                ),
                new ElementTypeVerifier(
                        type -> type.size % type.offset == 0,
                        (size, offset) -> String.format("size(%s) must be a multiple of offset(%s)", size, offset)
                ),
                new ElementTypeVerifier(
                        type -> type.offset <= type.size,
                        (size, offset) -> String.format("offset(%s) must be <= size(%s)", offset, size)
                )
        );
    }
}
