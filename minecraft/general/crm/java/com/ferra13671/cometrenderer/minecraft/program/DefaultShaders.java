package com.ferra13671.cometrenderer.minecraft.program;

import com.ferra13671.cometrenderer.CometLoaders;
import com.ferra13671.cometrenderer.glsl.shader.GlShader;
import com.ferra13671.cometrenderer.glsl.shader.ShaderType;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;
import lombok.experimental.UtilityClass;
import org.apiguardian.api.API;

@API(status = API.Status.MAINTAINED, since = "2.7")
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
            .build();

    public final GlShader BLUR_FRAME_FRAGMENT = CometLoaders.IN_JAR.createShaderBuilder()
            .info(
                    "shader.blur-frame.fragment",
                    "assets/crm/shaders/blur-frame.frag",
                    ShaderType.Fragment
            )
            .sampler("u_Texture")
            .uniform("weights", UniformType.FLOAT_ARRAY)
            .uniform("offsets", UniformType.VEC2)
            .uniform("radius", UniformType.INT)
            .uniform("texelSize", UniformType.VEC2)
            .build();

    public final GlShader ROUNDED_BLUR_VERTEX = CometLoaders.IN_JAR.createShaderBuilder()
            .info(
                    "shader.rounded-blur.vertex",
                    "assets/crm/shaders/rounded-blur.vert",
                    ShaderType.Vertex
            )
            .build();

    public final GlShader ROUNDED_BLUR_FRAGMENT = CometLoaders.IN_JAR.createShaderBuilder()
            .info(
                    "shader.rounded-blur.fragment",
                    "assets/crm/shaders/rounded-blur.frag",
                    ShaderType.Fragment
            )
            .sampler("u_Texture")
            .build();

    public final GlShader BLIT_FRAGMENT = CometLoaders.IN_JAR.createShaderBuilder()
            .info(
                    "shader.blit.fragment",
                    "assets/crm/shaders/blit.frag",
                    ShaderType.Fragment
            )
            .sampler("u_Texture")
            .build();

    public final GlShader LIQUID_GLASS_FRAGMENT = CometLoaders.IN_JAR.createShaderBuilder()
            .info(
                    "shader.liquid-glass.fragment",
                    "assets/crm/shaders/liquid-glass.frag",
                    ShaderType.Fragment
            )
            .sampler("u_Texture")
            .uniform("texelFetch", UniformType.VEC2)
            .build();
}
