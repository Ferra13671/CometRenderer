package com.ferra13671.cometrenderer.plugins.bettercompiler.test;

import com.ferra13671.cometrenderer.CometLoaders;
import com.ferra13671.cometrenderer.CometRenderer;
import com.ferra13671.cometrenderer.CometTags;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import com.ferra13671.cometrenderer.plugins.bettercompiler.BetterCompilerPlugin;
import com.ferra13671.cometrenderer.plugins.bettercompiler.BetterCompilerTags;
import com.ferra13671.cometrenderer.plugins.bettercompiler.GlShaderLibraryBuilder;
import com.ferra13671.cometrenderer.utils.GLVersion;
import lombok.experimental.UtilityClass;

import java.nio.file.Path;
import java.nio.file.Paths;

@UtilityClass
public class Main {
    private final Path testsPath = Paths.get("plugins/better-compiler/tests_output");

    public void main(String[] args) {
        CometRenderer.getRegistry().set(CometTags.GL_VERSION, GLVersion.GL40);
        BetterCompilerPlugin.init();

        BetterCompilerPlugin.registerShaderLibraries(
                new GlShaderLibraryBuilder<>(CometLoaders.IN_JAR)
                        .name("exampleLib1")
                        .library("exampleLibrary1.glsl")
                        .uniform("shaderColor", UniformType.VEC4)
                        .build(),
                new GlShaderLibraryBuilder<>(CometLoaders.IN_JAR)
                        .name("exampleLib2")
                        .library("exampleLibrary2.glsl")
                        .uniform("Projection", UniformType.BUFFER)
                        .uniform("modelViewMat", UniformType.MATRIX4)
                        .build(),
                new GlShaderLibraryBuilder<>(CometLoaders.IN_JAR)
                        .name("exampleLib3")
                        .library("exampleLibrary3.glsl")
                        .build()
        );

        testGlslVersionDirective();
        testConstantDirective();
        testMethodFeatures();
        testShaderLibraryDirective();
        testInterfaceBlocksDirectives();
    }

    public void testGlslVersionDirective() {
        startGroup(new TestGroup(
                "Glsl version directive",
                new Test(
                        "Auto glsl version",
                        "autoGlslVersionTest.vsh",
                        shaderRegistry -> {},
                        programRegistry -> {}
                ),
                new Test(
                        "Redirect glsl version",
                        "redirectGlslVersionTest.vsh",
                        shaderRegistry -> {},
                        programRegistry ->
                                programRegistry.set(BetterCompilerTags.GLSL_VERSION, GLVersion.GL46)
                ),
                new Test(
                        "Paste glsl version",
                        "pasteGlslVersionTest.vsh",
                        shaderRegistry -> {},
                        programRegistry ->
                                programRegistry.set(BetterCompilerTags.GLSL_VERSION, CometRenderer.getRegistry().get(CometTags.GL_VERSION).orElseThrow().getValue())
                )
        ));
    }

    public void testConstantDirective() {
        startGroup(new TestGroup(
                "Constant directive",
                new Test(
                        "Constant without default value",
                        "constantTest.vsh",
                        shaderRegistry -> {},
                        programRegistry ->
                                programRegistry.get(BetterCompilerTags.PROGRAM_INFO).orElseThrow().getValue().defineConstant("value", "1f")
                ),
                new Test(
                        "Constant with default value",
                        "constantWithDefaultValueTest.vsh",
                        shaderRegistry -> {},
                        programRegistry -> {}
                )
        ));
    }

    public void testMethodFeatures() {
        startGroup(new TestGroup(
                "Method features",
                new Test(
                        "Abstract method directive",
                        "abstractMethodTest.vsh",
                        shaderRegistry -> {},
                        programRegistry ->
                                programRegistry.get(BetterCompilerTags.PROGRAM_INFO).orElseThrow().getValue().defineMethod(
                                        "main",
                                        """
                                                gl_Position = projMat * modelViewMat * position;
                                                """
                                )
                )
        ));
    }

    public void testShaderLibraryDirective() {
        startGroup(new TestGroup(
                "Shader library directive",
                new Test(
                        "Default shader library include",
                        "defaultShaderLibraryTest.vsh",
                        shaderRegistry -> {},
                        programRegistry -> {}
                ),
                new Test(
                        "Multi shader library include",
                        "multiShaderLibraryTest.vsh",
                        shaderRegistry -> {},
                        programRegistry -> {}
                )
        ));
    }

    public void testInterfaceBlocksDirectives() {
        startGroup(new TestGroup(
                "interface blocks directives",
                new Test(
                        "Default directives",
                        "defaultInterfaceBlocksTest.vsh",
                        shaderRegistry -> {},
                        programRegistry -> {}
                ),
                new Test(
                        "Inouts directive",
                        "inoutsDirectiveTest.vsh",
                        shaderRegistry -> {},
                        programRegistry -> {}
                )
        ));
    }

    public void startGroup(TestGroup testGroup) {
        testGroup.startAndLog(testsPath.resolve(testGroup.getName() + ".txt").toFile());
    }
}
