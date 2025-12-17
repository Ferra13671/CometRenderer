package com.ferra13671.cometrenderer;

public record Mesa3DVersion(String version) {
    public static final Mesa3DVersion NONE = new Mesa3DVersion("-1");

    public static Mesa3DVersion fromString(String version, String vendor) {
        if (!"Mesa".contains(vendor))
            return NONE;

        return new Mesa3DVersion(version.substring(version.lastIndexOf("Mesa")).replace("Mesa ", ""));
    }
}
