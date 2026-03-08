package com.ferra13671.cometrenderer.minecraft.program;

import com.ferra13671.cometrenderer.CometLoaders;
import com.ferra13671.cometrenderer.glsl.shader.GlShader;
import com.ferra13671.cometrenderer.glsl.shader.ShaderType;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DefaultShaders {
    public final GlShader POSITION_VERTEX = CometLoaders.IN_JAR.createShaderBuilder()
            .info(
                    "shader.position.vertex",
                    "assets/crm/shaders/position.vert",
                    ShaderType.Vertex
            )
            .build();
    public final GlShader POSITION_FRAGMENT = CometLoaders.IN_JAR.createShaderBuilder()
            .info(
                    "shader.position.fragment",
                    "assets/crm/shaders/position.frag",
                    ShaderType.Fragment
            )
            .build();
    public final GlShader POSITION_COLOR_VERTEX = CometLoaders.IN_JAR.createShaderBuilder()
            .info(
                    "shader.position-color.vertex",
                    "assets/crm/shaders/position-color.vert",
                    ShaderType.Vertex
            )
            .build();
    public final GlShader POSITION_COLOR_FRAGMENT = CometLoaders.IN_JAR.createShaderBuilder()
            .info(
                    "shader.position-color.fragment",
                    "assets/crm/shaders/position-color.frag",
                    ShaderType.Fragment
            )
            .build();
    public final GlShader POSITION_TEXTURE_VERTEX = CometLoaders.IN_JAR.createShaderBuilder()
            .info(
                    "shader.position-texture.vertex",
                    "assets/crm/shaders/position-texture.vert",
                    ShaderType.Vertex
            )
            .build();
    public final GlShader POSITION_TEXTURE_FRAGMENT = CometLoaders.IN_JAR.createShaderBuilder()
            .info(
                    "shader.position-texture.fragment",
                    "assets/crm/shaders/position-texture.frag",
                    ShaderType.Fragment
            )
            .sampler("u_Texture")
            .build();
    public final GlShader POSITION_TEXTURE_COLOR_VERTEX = CometLoaders.IN_JAR.createShaderBuilder()
            .info(
                    "shader.position-texture-color.vertex",
                    "assets/crm/shaders/position-texture-color.vert",
                    ShaderType.Vertex
            )
            .build();
    public final GlShader POSITION_TEXTURE_COLOR_FRAGMENT = CometLoaders.IN_JAR.createShaderBuilder()
            .info(
                    "shader.position-texture-color.fragment",
                    "assets/crm/shaders/position-texture-color.frag",
                    ShaderType.Fragment
            )
            .sampler("u_Texture")
            .build();
    public final GlShader ROUNDED_RECT_VERTEX = CometLoaders.IN_JAR.createShaderBuilder()
            .info(
                    "shader.rounded-rect.vertex",
                    "assets/crm/shaders/rounded-rect.vert",
                    ShaderType.Vertex
            )
            .build();
    public final GlShader ROUNDED_RECT_FRAGMENT = CometLoaders.IN_JAR.createShaderBuilder()
            .info(
                    "shader.rounded-rect.fragment",
                    "assets/crm/shaders/rounded-rect.frag",
                    ShaderType.Fragment
            )
            .uniform("height", UniformType.FLOAT)
            .build();
    public final GlShader ROUNDED_TEXTURE_VERTEX = CometLoaders.IN_JAR.createShaderBuilder()
            .info(
                    "shader.rounded-texture.vertex",
                    "assets/crm/shaders/rounded-texture.vert",
                    ShaderType.Vertex
            )
            .build();
    public final GlShader ROUNDED_TEXTURE_FRAGMENT = CometLoaders.IN_JAR.createShaderBuilder()
            .info(
                    "shader.rounded-texture.fragment",
                    "assets/crm/shaders/rounded-texture.frag",
                    ShaderType.Fragment
            )
            .sampler("u_Texture")
            .uniform("height", UniformType.FLOAT)
            .build();
}
