package com.ferra13671.cometrenderer.plugins.bettercompiler.test;

import com.ferra13671.cometrenderer.CometLoaders;
import com.ferra13671.cometrenderer.glsl.compiler.GlobalCometCompiler;
import com.ferra13671.cometrenderer.glsl.compiler.GlslFileEntry;
import com.ferra13671.cometrenderer.plugins.bettercompiler.BetterCompilerProgramInfo;
import com.ferra13671.cometrenderer.plugins.bettercompiler.BetterCompilerTags;
import com.ferra13671.cometrenderer.utils.tag.Registry;

import java.io.PrintWriter;
import java.util.function.Consumer;

public record Test(String name, String shaderName, Consumer<Registry> shaderRegistryConsumer, Consumer<Registry> programRegistryConsumer) {

    public void startAndLog(PrintWriter printWriter) {
        GlslFileEntry fileEntry = CometLoaders.IN_JAR.createGlslFileEntry(shaderName, shaderName);
        String beforeProcessContent = fileEntry.getContent().concatLines();

        Registry registry = new Registry();
        shaderRegistryConsumer.accept(fileEntry.getRegistry());
        registry.set(BetterCompilerTags.PROGRAM_INFO, new BetterCompilerProgramInfo()); //The plugin will automatically add this tag to the program registry, but here we need to add it ourselves.
        programRegistryConsumer.accept(registry);
        GlobalCometCompiler.processContent(fileEntry.getRegistry(), registry);

        printWriter.println("\n\n");
        printWriter.println(String.format("Test '%s'", this.name));
        printWriter.println("\n//********** Before process **********//");
        printWriter.println(beforeProcessContent);
        printWriter.println("\n//********** After process **********//");
        printWriter.println(fileEntry.getContent().concatLines());
        printWriter.println("Test finished");
        printWriter.println("\n\n");
    }
}
