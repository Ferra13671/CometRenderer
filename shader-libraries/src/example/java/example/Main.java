package example;

import com.ferra13671.cometrenderer.CometLoaders;
import com.ferra13671.cometrenderer.compiler.GlslFileEntry;
import com.ferra13671.cometrenderer.plugins.shaderlibraries.GlShaderLibraryBuilder;
import com.ferra13671.cometrenderer.plugins.shaderlibraries.ShaderLibrariesPlugin;
import com.ferra13671.cometrenderer.program.uniform.UniformType;

public class Main {

    public static void main(String[] args) {
        GlslFileEntry shaderLib = new GlShaderLibraryBuilder<>(CometLoaders.IN_JAR)
                .name("exampleLib")
                .library("exampleLibrary.glsl")
                .uniform("Projection", UniformType.BUFFER)
                .uniform("modelViewMat", UniformType.MATRIX4)
                .build();
        ShaderLibrariesPlugin.registerShaderLibraries(
                shaderLib
        );

        GlslFileEntry shaderEntry = CometLoaders.IN_JAR.createGlslFileEntry("exampleShader", "exampleShader.vsh");
        ShaderLibrariesPlugin.includeShaderLibraries(shaderEntry.getRegistry());
        System.out.println(shaderEntry.getContent());
    }
}
