package com.ferra13671.cometrenderer.plugins.bettercompiler.utils;

public class StringExtension {

    public static int charCount(String string, char needChar) {
        return Math.toIntExact(string.chars().filter(ch -> ch == needChar).count());
    }
}
