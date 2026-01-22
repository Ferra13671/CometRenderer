package com.ferra13671.cometrenderer.utils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum GLVersion {
    GL10("1.0", null, 10),
    GL11("1.1", null, 11),
    GL12("1.2", null, 12),
    GL13("1.3", null, 13),
    GL14("1.4", null, 14),
    GL15("1.5",null, 15),
    GL20("2.0", "110", 20),
    GL21("2.1", "120", 21),
    GL30("3.0", "130", 30),
    GL31("3.1", "140", 31),
    GL32("3.2", "150", 32),
    GL33("3.3", "330 core", 33),
    GL40("4.0", "400 core", 40),
    GL41("4.1", "410 core", 41),
    GL42("4.2", "420 core", 42),
    GL43("4.3", "430 core", 43),
    GL44("4.4", "440 core", 44),
    GL45("4.5", "450 core", 45),
    GL46("4.6", "460 core", 46);

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
