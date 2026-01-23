package com.ferra13671.cometrenderer.plugins.bettercompiler.test;

import com.ferra13671.cometrenderer.CometLoaders;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.glsl.compiler.GlobalCometCompiler;
import com.ferra13671.cometrenderer.glsl.compiler.GlslDirectiveProcessor;
import com.ferra13671.cometrenderer.glsl.compiler.GlslFileEntry;
import com.ferra13671.cometrenderer.plugins.bettercompiler.BetterCompilerPlugin;
import com.ferra13671.cometrenderer.utils.GLVersion;
import com.ferra13671.cometrenderer.utils.tag.Registry;

import java.awt.*;

public class Main {

    public static void main(String[] args) {
        CometRenderer.getRegistry().set(CometTags.GL_VERSION, GLVersion.GL40);
        new BetterCompilerPlugin();

        testGlslVersionDirective();
    }

    public static void testGlslVersionDirective() {
        testAutoGlslVersion();
        testRedirectGlslVersion();
        testPasteGlslVersion();
    }

    public static void testAutoGlslVersion() {
        GlslFileEntry fileEntry = CometLoaders.IN_JAR.createGlslFileEntry("autoGlslVersionTest.vsh", "autoGlslVersionTest.vsh");
        String content = fileEntry.getContent();

        applyPlugin(fileEntry.getRegistry(), new Registry());

        printTest("Auto glsl version", content, fileEntry.getContent());
    }

    public static void testRedirectGlslVersion() {
        GlslFileEntry fileEntry = CometLoaders.IN_JAR.createGlslFileEntry("redirectGlslVersionTest.vsh", "redirectGlslVersionTest.vsh");
        String content = fileEntry.getContent();

        Registry registry = new Registry();
        registry.set(BetterCompilerPlugin.GLSL_VERSION, GLVersion.GL46);
        applyPlugin(fileEntry.getRegistry(), registry);

        printTest("Redirect glsl version", content, fileEntry.getContent());
    }

    public static void testPasteGlslVersion() {
        GlslFileEntry fileEntry = CometLoaders.IN_JAR.createGlslFileEntry("pasteGlslVersionTest.vsh", "pasteGlslVersionTest.vsh");
        String content = fileEntry.getContent();

        Registry registry = new Registry();
        registry.set(BetterCompilerPlugin.GLSL_VERSION, CometRenderer.getRegistry().get(CometTags.GL_VERSION).orElseThrow().getValue());
        applyPlugin(fileEntry.getRegistry(), registry);

        printTest("Paste glsl version", content, fileEntry.getContent());
    }

    public static void println(String s) {
        System.out.println(s + ColorCode.RESET);
    }

    public static void printTest(String name, String beforeProcess, String afterProcess) {
        println(ColorCode.CYAN + "Test: " + name);
        println(ColorCode.GREEN + "Before process:");
        println(ColorCode.YELLOW + beforeProcess);
        println(ColorCode.GREEN + "After process:");
        println(ColorCode.YELLOW + afterProcess);
        println(ColorCode.PURPLE + "Test finished\n\n");
    }

    public static void applyPlugin(Registry shaderRegistry, Registry programRegistry) {
        GlslDirectiveProcessor.processContent(shaderRegistry, programRegistry);
        GlobalCometCompiler.applyCompileExtensions(shaderRegistry, programRegistry);
    }
}
