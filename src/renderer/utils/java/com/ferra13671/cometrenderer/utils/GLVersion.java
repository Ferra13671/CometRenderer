package com.ferra13671.cometrenderer.utils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum GLVersion {
    GL10("1.0", null, 0),
    GL11("1.1", null, 1),
    GL12("1.2", null, 2),
    GL13("1.3", null, 3),
    GL14("1.4", null, 4),
    GL15("1.5",null, 5),
    GL20("2.0", "110", 6),
    GL21("2.1", "120", 7),
    GL30("3.0", "130", 8),
    GL31("3.1", "140", 9),
    GL32("3.2", "150", 10),
    GL33("3.3", "330 core", 11),
    GL40("4.0", "400 core", 12),
    GL41("4.1", "410 core", 13),
    GL42("4.2", "420 core", 14),
    GL43("4.3", "430 core", 15),
    GL44("4.4", "440 core", 16),
    GL45("4.5", "450 core", 17),
    GL46("4.6", "460 core", 18);

    public final String glVersion;
    public final String glslVersion;
    public final int id;

    public static GLVersion fromString(String version) {
        for (GLVersion value : values())
            if (version.startsWith(value.glVersion))
                return value;

        return null;
    }
}
