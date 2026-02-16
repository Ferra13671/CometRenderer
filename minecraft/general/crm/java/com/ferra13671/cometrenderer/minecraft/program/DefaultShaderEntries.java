package com.ferra13671.cometrenderer.minecraft.program;

import com.ferra13671.cometrenderer.CometLoaders;
import com.ferra13671.cometrenderer.glsl.compiler.GlslFileEntry;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DefaultShaderEntries {
    public final GlslFileEntry POSITION_VERTEX = CometLoaders.IN_JAR.createGlslFileEntry(
            "shader.position.vertex",
            "assets/crm/shaders/position.vert"
    );
    public final GlslFileEntry POSITION_FRAGMENT = CometLoaders.IN_JAR.createGlslFileEntry(
            "shader.position.fragment",
            "assets/crm/shaders/position.frag"
    );
    public final GlslFileEntry POSITION_COLOR_VERTEX = CometLoaders.IN_JAR.createGlslFileEntry(
            "shader.position-color.vertex",
            "assets/crm/shaders/position-color.vert"
    );
    public final GlslFileEntry POSITION_COLOR_FRAGMENT = CometLoaders.IN_JAR.createGlslFileEntry(
            "shader.position-color.fragment",
            "assets/crm/shaders/position-color.frag"
    );
    public final GlslFileEntry POSITION_TEXTURE_VERTEX = CometLoaders.IN_JAR.createGlslFileEntry(
            "shader.position-texture.vertex",
            "assets/crm/shaders/position-texture.vert"
    );
    public final GlslFileEntry POSITION_TEXTURE_FRAGMENT = CometLoaders.IN_JAR.createGlslFileEntry(
            "shader.position-texture.fragment",
            "assets/crm/shaders/position-texture.frag"
    );
    public final GlslFileEntry POSITION_TEXTURE_COLOR_VERTEX = CometLoaders.IN_JAR.createGlslFileEntry(
            "shader.position-texture-color.vertex",
            "assets/crm/shaders/position-texture-color.vert"
    );
    public final GlslFileEntry POSITION_TEXTURE_COLOR_FRAGMENT = CometLoaders.IN_JAR.createGlslFileEntry(
            "shader.position-texture-color.fragment",
            "assets/crm/shaders/position-texture-color.frag"
    );
    public final GlslFileEntry ROUNDED_RECT_VERTEX = CometLoaders.IN_JAR.createGlslFileEntry(
            "shader.rounded-rect.vertex",
            "assets/crm/shaders/rounded-rect.vert"
    );
    public final GlslFileEntry ROUNDED_RECT_FRAGMENT = CometLoaders.IN_JAR.createGlslFileEntry(
            "shader.rounded-rect.fragment",
            "assets/crm/shaders/rounded-rect.frag"
    );
    public final GlslFileEntry ROUNDED_TEXTURE_VERTEX = CometLoaders.IN_JAR.createGlslFileEntry(
            "shader.rounded-texture.vertex",
            "assets/crm/shaders/rounded-texture.vert"
    );
    public final GlslFileEntry ROUNDED_TEXTURE_FRAGMENT = CometLoaders.IN_JAR.createGlslFileEntry(
            "shader.rounded-texture.fragment",
            "assets/crm/shaders/rounded-texture.frag"
    );
}
