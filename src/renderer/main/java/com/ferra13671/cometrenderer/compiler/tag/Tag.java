package com.ferra13671.cometrenderer.compiler.tag;

public record Tag<T>(String id) {

    public T map(Object value) {
        return (T) value;
    }
}
