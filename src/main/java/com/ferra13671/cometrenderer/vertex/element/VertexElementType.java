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
import java.util.function.Function;

/**
 * Тип данных, передаваемых элементу.
 * Хотя OpenGL поддерживает только примитивные типы, но при помощи данного класса можно написать свои типы данных.
 *
 * @param name имя типа данных.
 * @param offset смещение в байтах между примитивными элементами в буффере. Значение должно быть <=size и > 0, а так же size должен быть кратен offset. Позволяет определять внутри элементами несколько примитивных элементов (Например несколько float чисел в одном элементе).
 * @param primitivesCount количество примитивных элементов в типе.
 * @param primitivesPerAttribute количество примитивных элементов в одном атрибуте
 *                              (необходимо для разделения 3x3x3 для mat3, без данного параметра невозможно было бы определить то, как распределять примитивы между аттрибутами,
 *                               и вышло бы так, что mat3 тип разделялся на 4x4x1).
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
public record VertexElementType<T>(String name, int offset, int primitivesCount, int primitivesPerAttribute, int glId, Class<T> clazz, BiConsumer<Long, T[]> uploadConsumer) {
    public static final VertexElementType<Float> FLOAT = builder(Float.class)
            .name("Float")
            .offset(4)
            .primitivesCount(1)
            .primitivesPerAttribute(4)
            .glId(GL11.GL_FLOAT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++)
                            MemoryUtil.memPutFloat(pointer + (4L * i), data[i]);
                    }
            )
            .build();
    public static final VertexElementType<Float> FLOAT_ARRAY = FLOAT.toBuilder()
            .name("Float Array")
            .primitivesPerAttribute(1)
            .build();
    public static final VertexElementType<Integer> INT = builder(Integer.class)
            .name("Int")
            .offset(4)
            .primitivesCount(1)
            .primitivesPerAttribute(4)
            .glId(GL11.GL_INT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++)
                            MemoryUtil.memPutInt(pointer + (4L * i), data[i]);
                    }
            )
            .build();
    public static final VertexElementType<Integer> INT_ARRAY = INT.toBuilder()
            .name("Int Array")
            .primitivesPerAttribute(1)
            .build();
    public static final VertexElementType<Short> SHORT = builder(Short.class)
            .name("Short")
            .offset(2)
            .primitivesCount(1)
            .primitivesPerAttribute(4)
            .glId(GL11.GL_SHORT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++)
                            MemoryUtil.memPutShort(pointer + (2L * i), data[i]);
                    }
            )
            .build();
    public static final VertexElementType<Short> SHORT_ARRAY = SHORT.toBuilder()
            .name("Short Array")
            .primitivesPerAttribute(1)
            .build();
    public static final VertexElementType<Byte> BYTE = builder(Byte.class)
            .name("Byte")
            .offset(1)
            .primitivesCount(1)
            .primitivesPerAttribute(4)
            .glId(GL11.GL_BYTE)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++)
                            MemoryUtil.memPutByte(pointer + i, data[i]);
                    }
            )
            .build();
    public static final VertexElementType<Byte> BYTE_ARRAY = BYTE.toBuilder()
            .name("Byte Array")
            .primitivesPerAttribute(1)
            .build();
    public static final VertexElementType<Integer> UNSIGNED_INT = builder(Integer.class)
            .name("Unsigned Int")
            .offset(4)
            .primitivesCount(1)
            .primitivesPerAttribute(4)
            .glId(GL11.GL_UNSIGNED_INT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++)
                            MemoryUtil.memPutInt(pointer + (4L * i), data[i]);
                    }
            )
            .build();
    public static final VertexElementType<Integer> UNSIGNED_INT_ARRAY = UNSIGNED_INT.toBuilder()
            .name("Unsigned Int Array")
            .primitivesPerAttribute(1)
            .build();
    public static final VertexElementType<Short> UNSIGNED_SHORT = builder(Short.class)
            .name("Unsigned Short")
            .offset(2)
            .primitivesCount(1)
            .primitivesPerAttribute(4)
            .glId(GL11.GL_UNSIGNED_SHORT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++)
                            MemoryUtil.memPutShort(pointer + (2L * i), data[i]);
                    }
            )
            .build();
    public static final VertexElementType<Short> UNSIGNED_SHORT_ARRAY = UNSIGNED_SHORT.toBuilder()
            .name("Unsigned Short Array")
            .primitivesPerAttribute(1)
            .build();
    public static final VertexElementType<Byte> UNSIGNED_BYTE = builder(Byte.class)
            .name("Unsigned Byte")
            .offset(1)
            .primitivesCount(1)
            .primitivesPerAttribute(4)
            .glId(GL11.GL_UNSIGNED_BYTE)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++)
                            MemoryUtil.memPutByte(pointer + i, data[i]);
                    }
            )
            .build();
    public static final VertexElementType<Byte> UNSIGNED_BYTE_ARRAY = UNSIGNED_BYTE.toBuilder()
            .name("Unsigned Byte Array")
            .primitivesPerAttribute(1)
            .build();
    public static final VertexElementType<Vector2ic> VECTOR_2_INT = builder(Vector2ic.class)
            .name("Vector2i")
            .offset(4)
            .primitivesCount(2)
            .primitivesPerAttribute(2)
            .glId(GL11.GL_INT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            long p = pointer + (8L * i);

                            MemoryUtil.memPutInt(p, data[i].x());
                            MemoryUtil.memPutInt(p + 4L, data[i].y());
                        }
                    }
            )
            .build();
    public static final VertexElementType<Vector2ic> UNSIGNED_VECTOR_2_INT = builder(Vector2ic.class)
            .name("Unsigned Vector2i")
            .offset(4)
            .primitivesCount(2)
            .primitivesPerAttribute(2)
            .glId(GL11.GL_UNSIGNED_INT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            long p = pointer + (8L * i);

                            MemoryUtil.memPutInt(p, data[i].x());
                            MemoryUtil.memPutInt(p + 4L, data[i].y());
                        }
                    }
            )
            .build();
    public static final VertexElementType<Vector2fc> VECTOR_2_FLOAT = builder(Vector2fc.class)
            .name("Vector2f")
            .offset(4)
            .primitivesCount(2)
            .primitivesPerAttribute(2)
            .glId(GL11.GL_FLOAT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            long p = pointer + (8L * i);

                            MemoryUtil.memPutFloat(p, data[i].x());
                            MemoryUtil.memPutFloat(p + 4L, data[i].y());
                        }
                    }
            )
            .build();
    public static final VertexElementType<Vector3ic> VECTOR_3_INT = builder(Vector3ic.class)
            .name("Vector3i")
            .offset(4)
            .primitivesCount(3)
            .primitivesPerAttribute(3)
            .glId(GL11.GL_INT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            long p = pointer + (12L * i);

                            MemoryUtil.memPutInt(p, data[i].x());
                            MemoryUtil.memPutInt(p + 4L, data[i].y());
                            MemoryUtil.memPutInt(p + 8L, data[i].z());
                        }
                    }
            )
            .build();
    public static final VertexElementType<Vector3ic> UNSIGNED_VECTOR_3_INT = builder(Vector3ic.class)
            .name("Unsigned Vector3i")
            .offset(4)
            .primitivesCount(3)
            .primitivesPerAttribute(3)
            .glId(GL11.GL_UNSIGNED_INT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            long p = pointer + (12L * i);

                            MemoryUtil.memPutInt(p, data[i].x());
                            MemoryUtil.memPutInt(p + 4L, data[i].y());
                            MemoryUtil.memPutInt(p + 8L, data[i].z());
                        }
                    }
            )
            .build();
    public static final VertexElementType<Vector3fc> VECTOR_3_FLOAT = builder(Vector3fc.class)
            .name("Vector3f")
            .offset(4)
            .primitivesCount(3)
            .primitivesPerAttribute(3)
            .glId(GL11.GL_FLOAT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            long p = pointer + (12L * i);

                            MemoryUtil.memPutFloat(p, data[i].x());
                            MemoryUtil.memPutFloat(p + 4L, data[i].y());
                            MemoryUtil.memPutFloat(p + 8L, data[i].z());
                        }
                    }
            )
            .build();
    public static final VertexElementType<Vector4ic> VECTOR_4_INT = builder(Vector4ic.class)
            .name("Vector4i")
            .offset(4)
            .primitivesCount(4)
            .primitivesPerAttribute(4)
            .glId(GL11.GL_INT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            long p = pointer + (16L * i);

                            MemoryUtil.memPutInt(p, data[i].x());
                            MemoryUtil.memPutInt(p + 4L, data[i].y());
                            MemoryUtil.memPutInt(p + 8L, data[i].z());
                            MemoryUtil.memPutInt(p + 12L, data[i].w());
                        }
                    }
            )
            .build();
    public static final VertexElementType<Vector4ic> UNSIGNED_VECTOR_4_INT = builder(Vector4ic.class)
            .name("Unsigned Vector4i")
            .offset(4)
            .primitivesCount(4)
            .primitivesPerAttribute(4)
            .glId(GL11.GL_UNSIGNED_INT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            long p = pointer + (16L * i);

                            MemoryUtil.memPutInt(p, data[i].x());
                            MemoryUtil.memPutInt(p + 4L, data[i].y());
                            MemoryUtil.memPutInt(p + 8L, data[i].z());
                            MemoryUtil.memPutInt(p + 12L, data[i].w());
                        }
                    }
            )
            .build();
    public static final VertexElementType<Vector4fc> VECTOR_4_FLOAT = builder(Vector4fc.class)
            .name("Vector4f")
            .offset(4)
            .primitivesCount(4)
            .primitivesPerAttribute(4)
            .glId(GL11.GL_FLOAT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            long p = pointer + (16L * i);

                            MemoryUtil.memPutFloat(p, data[i].x());
                            MemoryUtil.memPutFloat(p + 4L, data[i].y());
                            MemoryUtil.memPutFloat(p + 8L, data[i].z());
                            MemoryUtil.memPutFloat(p + 12L, data[i].w());
                        }
                    }
            )
            .build();
    @API(status = API.Status.MAINTAINED, since = "3.1")
    public static final VertexElementType<Matrix3fc> MATRIX_3_FLOAT = builder(Matrix3fc.class)
            .name("Matrix3f")
            .offset(4)
            .primitivesCount(9)
            .primitivesPerAttribute(3)
            .glId(GL11.GL_FLOAT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            long p = pointer + (36L * i);

                            MemoryUtil.memPutFloat(p, data[i].m00());
                            MemoryUtil.memPutFloat(p + 4L, data[i].m01());
                            MemoryUtil.memPutFloat(p + 8L, data[i].m02());
                            MemoryUtil.memPutFloat(p + 12L, data[i].m10());
                            MemoryUtil.memPutFloat(p + 16L, data[i].m11());
                            MemoryUtil.memPutFloat(p + 20L, data[i].m12());
                            MemoryUtil.memPutFloat(p + 24L, data[i].m20());
                            MemoryUtil.memPutFloat(p + 28L, data[i].m21());
                            MemoryUtil.memPutFloat(p + 32L, data[i].m22());
                        }
                    }
            )
            .build();
    @API(status = API.Status.MAINTAINED, since = "3.1")
    public static final VertexElementType<Matrix4fc> MATRIX_4_FLOAT = builder(Matrix4fc.class)
            .name("Matrix4f")
            .offset(4)
            .primitivesCount(16)
            .primitivesPerAttribute(4)
            .glId(GL11.GL_FLOAT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            long p = pointer + (64L * i);

                            MemoryUtil.memPutFloat(p, data[i].m00());
                            MemoryUtil.memPutFloat(p + 4L, data[i].m01());
                            MemoryUtil.memPutFloat(p + 8L, data[i].m02());
                            MemoryUtil.memPutFloat(p + 12L, data[i].m03());
                            MemoryUtil.memPutFloat(p + 16L, data[i].m10());
                            MemoryUtil.memPutFloat(p + 20L, data[i].m11());
                            MemoryUtil.memPutFloat(p + 24L, data[i].m12());
                            MemoryUtil.memPutFloat(p + 28L, data[i].m13());
                            MemoryUtil.memPutFloat(p + 32L, data[i].m20());
                            MemoryUtil.memPutFloat(p + 36L, data[i].m21());
                            MemoryUtil.memPutFloat(p + 40L, data[i].m22());
                            MemoryUtil.memPutFloat(p + 44L, data[i].m23());
                            MemoryUtil.memPutFloat(p + 48L, data[i].m30());
                            MemoryUtil.memPutFloat(p + 52L, data[i].m31());
                            MemoryUtil.memPutFloat(p + 56L, data[i].m32());
                            MemoryUtil.memPutFloat(p + 60L, data[i].m33());
                        }
                    }
            )
            .build();
    public static final VertexElementType<Color> COLOR_FLOAT = builder(Color.class)
            .name("Color float")
            .offset(4)
            .primitivesCount(4)
            .primitivesPerAttribute(4)
            .glId(GL11.GL_FLOAT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            long p = pointer + (16L * i);

                            MemoryUtil.memPutFloat(p, data[i].getRed() / 255f);
                            MemoryUtil.memPutFloat(p + 4L, data[i].getGreen() / 255f);
                            MemoryUtil.memPutFloat(p + 8L, data[i].getBlue() / 255f);
                            MemoryUtil.memPutFloat(p + 12L, data[i].getAlpha() / 255f);
                        }
                    }
            )
            .build();
    public static final VertexElementType<Color> COLOR_INT = builder(Color.class)
            .name("Color int")
            .offset(4)
            .primitivesCount(4)
            .primitivesPerAttribute(4)
            .glId(GL11.GL_INT)
            .uploadConsumer(
                    (pointer, data) -> {
                        for (int i = 0; i < data.length; i++) {
                            long p = pointer + (16L * i);

                            MemoryUtil.memPutInt(p, data[i].getRed());
                            MemoryUtil.memPutInt(p + 4L, data[i].getGreen());
                            MemoryUtil.memPutInt(p + 8L, data[i].getBlue());
                            MemoryUtil.memPutInt(p + 12L, data[i].getAlpha());
                        }
                    }
            )
            .build();

    /**
     * Возвращает размер элемента в байтах.
     *
     * @return размер элемента в байтах.
     */
    public int size() {
        return this.offset * this.primitivesCount;
    }

    public void verify() {
        for (ElementTypeVerifier verifier : ElementTypeVerifier.verifiers)
            if (!verifier.verifyFunction.apply(this))
                ErrorHandlers.onIllegalVertexElementStructure(this, verifier.messageFunction.apply(this));
    }

    @API(status = API.Status.EXPERIMENTAL, since = "3.1")
    public VertexElementTypeBuilder<T> toBuilder() {
        return builder(this.clazz)
                .name(this.name)
                .offset(this.offset)
                .primitivesCount(this.primitivesCount)
                .primitivesPerAttribute(this.primitivesPerAttribute)
                .glId(this.glId)
                .uploadConsumer(this.uploadConsumer);
    }

    public static <T> VertexElementTypeBuilder<T> builder(Class<T> clazz) {
        return new VertexElementTypeBuilder<T>().clazz(clazz);
    }

    private record ElementTypeVerifier(Function<VertexElementType<?>, Boolean> verifyFunction, Function<VertexElementType<?>, String> messageFunction) {
        static final List<ElementTypeVerifier> verifiers = List.of(
                new ElementTypeVerifier(
                        type -> type.offset > 0,
                        type -> String.format("offset(%s) must be > 0", type.offset)
                ),
                new ElementTypeVerifier(
                        type -> type.primitivesCount > 0,
                        type -> String.format("primitivesCount(%s) must be > 0", type.primitivesCount)
                ),
                new ElementTypeVerifier(
                        type -> type.primitivesPerAttribute > 0,
                        type -> String.format("primitivesPerAttribute(%s) must be > 0", type.primitivesPerAttribute)
                ),
                new ElementTypeVerifier(
                        type -> type.primitivesPerAttribute <= 4,
                        type -> String.format("primitivesPerAttribute(%s) must be <= 4", type.primitivesPerAttribute)
                )
        );
    }
}
