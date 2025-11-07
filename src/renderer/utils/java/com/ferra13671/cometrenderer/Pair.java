package com.ferra13671.cometrenderer;

public class Pair<A, B> {
    private A left = null;
    private B right = null;

    public Pair(A left, B right) {
        this.left = left;
        this.right = right;
    }

    public Pair() {}

    public A getLeft() {
        return this.left;
    }

    public B getRight() {
        return this.right;
    }

    public void setLeft(A left) {
        this.left = left;
    }

    public void setRight(B right) {
        this.right = right;
    }
}
