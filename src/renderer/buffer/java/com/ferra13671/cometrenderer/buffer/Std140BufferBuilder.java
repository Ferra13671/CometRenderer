package com.ferra13671.cometrenderer.buffer;

import com.ferra13671.cometrenderer.utils.MathUtils;
import com.ferra13671.gltextureutils.Pair;
import lombok.Getter;
import org.joml.*;

import java.lang.Math;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Std140BufferBuilder {
    private final List<Pair<Integer, Object>> elements = new ArrayList<>();
    private int pos = 0;
    @Getter
    private int size = 0;
    private int offset = 0;

    public Std140BufferBuilder putByte(Byte b) {
        this.elements.addLast(new Pair<>(align(4), b));

        return this;
    }

    public Std140BufferBuilder putShort(Short sh) {
        this.elements.add(new Pair<>(align(4), sh));

        return this;
    }

    public Std140BufferBuilder putInt(Integer integer) {
        this.elements.add(new Pair<>(align(4), integer));

        return this;
    }

    public Std140BufferBuilder putFloat(Float fl) {
        this.elements.add(new Pair<>(align(4), fl));

        return this;
    }

    public Std140BufferBuilder putVec2(float x, float y) {
        return putVec2(new Vector2f(x, y));
    }

    public Std140BufferBuilder putVec2(Vector2fc vec2) {
        this.elements.add(new Pair<>(align(8), vec2));

        return this;
    }

    public Std140BufferBuilder putIVec2(int x, int y) {
        return putIVec2(new Vector2i(x, y));
    }

    public Std140BufferBuilder putIVec2(Vector2ic ivec2) {
        this.elements.add(new Pair<>(align(8), ivec2));

        return this;
    }

    public Std140BufferBuilder putVec3(float x, float y, float z) {
        return putVec3(new Vector3f(x, y, z));
    }

    public Std140BufferBuilder putVec3(Vector3fc vec3) {
        this.elements.add(new Pair<>(align(12), vec3));

        return this;
    }

    public Std140BufferBuilder putIVec3(int x, int y, int z) {
        return putIVec3(new Vector3i(x, y, z));
    }

    public Std140BufferBuilder putIVec3(Vector3ic ivec3) {
        this.elements.add(new Pair<>(align(12), ivec3));

        return this;
    }

    public Std140BufferBuilder putVec4(float x, float y, float z, float w) {
        return putVec4(new Vector4f(x, y, z, w));
    }

    public Std140BufferBuilder putVec4(Vector4fc vec4) {
        this.elements.add(new Pair<>(align(16), vec4));

        return this;
    }

    public Std140BufferBuilder putIVec4(int x, int y, int z, int w) {
        return putIVec4(new Vector4i(x, y, z, w));
    }

    public Std140BufferBuilder putIVec4(Vector4ic ivec4) {
        this.elements.add(new Pair<>(align(16), ivec4));

        return this;
    }

    public Std140BufferBuilder putMat4(Matrix4fc mat4) {
        putVec4(new Vector4f(mat4.m00(), mat4.m01(), mat4.m02(), mat4.m03()));
        putVec4(new Vector4f(mat4.m10(), mat4.m11(), mat4.m12(), mat4.m13()));
        putVec4(new Vector4f(mat4.m20(), mat4.m21(), mat4.m22(), mat4.m23()));
        return putVec4(new Vector4f(mat4.m30(), mat4.m31(), mat4.m32(), mat4.m33()));
    }

    public void uploadToBuffer(ByteBuffer buffer) {
        uploadToBuffer(buffer, 0);
    }

    public void uploadToBuffer(ByteBuffer buffer, int pos) {
        for (Pair<Integer, Object> element : this.elements) {
            buffer.position(pos + element.getLeft());

            Object o = element.getRight();

            if (o instanceof Byte b) {
                buffer.put(b);
            } else if (o instanceof Short sh) {
                buffer.putShort(sh);
            } else if (o instanceof Integer integer) {
                buffer.putInt(integer);
            } else if (o instanceof Float fl) {
                buffer.putFloat(fl);
            } else if (o instanceof Vector2fc vec2) {
                buffer
                        .putFloat(vec2.x())
                        .putFloat(vec2.y());
            } else if (o instanceof Vector2ic ivec2) {
                buffer
                        .putInt(ivec2.x())
                        .putInt(ivec2.y());
            } else if (o instanceof Vector3fc vec3) {
                buffer
                        .putFloat(vec3.x())
                        .putFloat(vec3.y())
                        .putFloat(vec3.z());
            } else if (o instanceof Vector3ic ivec3) {
                buffer
                        .putInt(ivec3.x())
                        .putInt(ivec3.y())
                        .putInt(ivec3.z());
            } else if (o instanceof Vector4fc vec4) {
                buffer
                        .putFloat(vec4.x())
                        .putFloat(vec4.y())
                        .putFloat(vec4.z())
                        .putFloat(vec4.w());
            } else if (o instanceof Vector4ic ivec4) {
                buffer
                        .putInt(ivec4.x())
                        .putInt(ivec4.y())
                        .putInt(ivec4.z())
                        .putInt(ivec4.w());
            }
        }
    }

    private int align(int size) {
        this.offset = Math.max(this.offset, size);

        this.pos = MathUtils.roundUpToMultiple(this.pos, size);
        int p = this.pos;
        this.pos += this.offset;
        this.size = MathUtils.roundUpToMultiple(this.size, size);
        this.size += this.offset;

        return p;
    }
}
