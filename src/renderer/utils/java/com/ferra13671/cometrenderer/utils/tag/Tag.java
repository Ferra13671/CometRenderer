package com.ferra13671.cometrenderer.utils.tag;

public record Tag<T>(String id) {

    public T map(Object value) {
        return (T) value;
    }
}
