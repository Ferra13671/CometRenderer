package com.ferra13671.cometrenderer.plugins.bettercompiler.test;

import com.ferra13671.cometrenderer.CometLoaders;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.glsl.compiler.GlobalCometCompiler;
import com.ferra13671.cometrenderer.glsl.compiler.GlslFileEntry;
import com.ferra13671.cometrenderer.plugins.bettercompiler.BetterCompilerPlugin;
import com.ferra13671.cometrenderer.plugins.bettercompiler.BetterCompilerProgramInfo;
import com.ferra13671.cometrenderer.plugins.bettercompiler.BetterCompilerTags;
import com.ferra13671.cometrenderer.utils.GLVersion;
import com.ferra13671.cometrenderer.utils.tag.Registry;

import java.util.function.Consumer;

public class Main {

    public static void main(String[] args) {
        CometRenderer.getRegistry().set(CometTags.GL_VERSION, GLVersion.GL40);
        BetterCompilerPlugin.init();

        testGlslVersionDirective();
        testConstantDirective();
    }

    public static void testGlslVersionDirective() {
        startGroupTest("Glsl version directive", () -> {
            startTest(
                    "Auto glsl version",
                    "autoGlslVersionTest.vsh",
                    shaderRegistry -> {},
                    programRegistry -> {}
            );
            startTest(
                    "Redirect glsl version",
                    "redirectGlslVersionTest.vsh",
                    shaderRegistry -> {},
                    programRegistry ->
                            programRegistry.set(BetterCompilerTags.GLSL_VERSION, GLVersion.GL46)
            );
            startTest(
                    "Paste glsl version",
                    "pasteGlslVersionTest.vsh",
                    shaderRegistry -> {},
                    programRegistry ->
                            programRegistry.set(BetterCompilerTags.GLSL_VERSION, CometRenderer.getRegistry().get(CometTags.GL_VERSION).orElseThrow().getValue())
            );
        });
    }

    public static void testConstantDirective() {
        startGroupTest("Constant directive", () -> {
            startTest(
                    "Constant without default value",
                    "constantTest.vsh",
                    shaderRegistry -> {},
                    programRegistry -> {
                        BetterCompilerProgramInfo info = new BetterCompilerProgramInfo();
                        programRegistry.set(BetterCompilerTags.PROGRAM_INFO, info); //The plugin will automatically add this tag to the program registry, but here we need to add it ourselves.

                        info.defineConstant("value", "1f");
                    }
            );
            startTest(
                    "Constant with default value",
                    "constantWithDefaultValueTest.vsh",
                    shaderRegistry -> {},
                    programRegistry -> programRegistry.set(BetterCompilerTags.PROGRAM_INFO, new BetterCompilerProgramInfo()) //The plugin will automatically add this tag to the program registry, but here we need to add it ourselves.
            );
        });
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
        println(ColorCode.PURPLE + "Test finished");
    }

    public static void startTest(String name, String shaderName, Consumer<Registry> shaderRegistryConsumer, Consumer<Registry> programRegistryConsumer) {
        GlslFileEntry fileEntry = CometLoaders.IN_JAR.createGlslFileEntry(shaderName, shaderName);
        String beforeProcessContent = fileEntry.getContent().concatLines();

        Registry registry = new Registry();
        shaderRegistryConsumer.accept(fileEntry.getRegistry());
        programRegistryConsumer.accept(registry);
        GlobalCometCompiler.processContent(fileEntry.getRegistry(), registry);

        printTest(name, beforeProcessContent, fileEntry.getContent().concatLines());
    }

    public static void startGroupTest(String groupName, Runnable groupRunnable) {
        println(ColorCode.RED + String.format("##### Test group: %s #####", groupName));
        groupRunnable.run();
        println(ColorCode.RED + "##### ----- #####");
    }
}
