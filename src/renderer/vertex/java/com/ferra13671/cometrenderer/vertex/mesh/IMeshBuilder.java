package com.ferra13671.cometrenderer.vertex.mesh;

import com.ferra13671.cometrenderer.vertex.element.VertexElementType;
import org.joml.Matrix4f;

public interface IMeshBuilder<T, G extends IMesh> {

    G buildNullable();

    G buildOrThrow();

    T vertex(float x, float y, float z);

    T vertex(Matrix4f matrix4f, float x, float y, float z);

    <S> T element(String name, VertexElementType<S> elementType, S... values);
}
