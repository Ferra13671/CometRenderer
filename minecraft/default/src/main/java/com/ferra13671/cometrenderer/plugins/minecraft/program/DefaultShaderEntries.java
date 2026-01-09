package com.ferra13671.cometrenderer.plugins.minecraft.program;

import com.ferra13671.cometrenderer.CometLoaders;
import com.ferra13671.cometrenderer.compiler.GlslFileEntry;
import lombok.experimental.UtilityClass;

//Ооооо да, пишем ебучие шейдеры в стринге как ебучие пастерки бля сигмо троллинг
@UtilityClass
public class DefaultShaderEntries {
    public final GlslFileEntry POSITION_VERTEX = CometLoaders.STRING.createGlslFileEntry(
            "shader.position.vertex",
            """
            #version 330 core
            
            in vec4 position;
            
            #include<matrices>
            
            void main() {
                gl_Position = projMat * modelViewMat * position;
            }
            """
    );
    public final GlslFileEntry POSITION_FRAGMENT = CometLoaders.STRING.createGlslFileEntry(
            "shader.position.fragment",
            """
            #version 330 core
            
            precision lowp float;
            
            #include<shaderColor>
            
            out vec4 fragColor;
            
            void main() {
                fragColor = shaderColor;
            }
            """
    );
    public final GlslFileEntry POSITION_COLOR_VERTEX = CometLoaders.STRING.createGlslFileEntry(
            "shader.position-color.vertex",
            """
            #version 330 core
            
            in vec4 pos;
            in vec4 color;
            
            #include<matrices>
            
            out vec4 vertexColor;
            
            void main() {
                gl_Position = projMat * modelViewMat * pos;
            
                vertexColor = color;
            }
            """
    );
    public final GlslFileEntry POSITION_COLOR_FRAGMENT = CometLoaders.STRING.createGlslFileEntry(
            "shader.position-color.fragment",
            """
            #version 330 core
            
            precision lowp float;
            
            in vec4 vertexColor;
            
            #include<shaderColor>
            
            out vec4 fragColor;
            
            void main() {
                fragColor = shaderColor * vertexColor;
            }
            """
    );
    public final GlslFileEntry POSITION_TEXTURE_VERTEX = CometLoaders.STRING.createGlslFileEntry(
            "shader.position-texture.vertex",
            """
            #version 330 core
            
            in vec4 position;
            in vec2 UV;
            
            #include<matrices>
            
            out vec2 texPos;
            
            void main() {
                gl_Position = projMat * modelViewMat * position;
            
                texPos = UV;
            }
            """
    );
    public final GlslFileEntry POSITION_TEXTURE_FRAGMENT = CometLoaders.STRING.createGlslFileEntry(
            "shader.position-texture.fragment",
            """
            #version 330 core
            
            precision lowp float;
            
            in vec2 texPos;
            
            #include<shaderColor>
            uniform sampler2D u_Texture;
            
            out vec4 fragColor;
            
            void main() {
                fragColor = texture(u_Texture, texPos) * shaderColor;
            }
            """
    );
    public final GlslFileEntry POSITION_TEXTURE_COLOR_VERTEX = CometLoaders.STRING.createGlslFileEntry(
            "shader.position-texture-color.vertex",
            """
            #version 330 core
            
            in vec4 pos;
            in vec2 UV;
            in vec4 color;
            
            #include<matrices>
            
            out vec2 texPos;
            out vec4 vertexColor;
            
            void main() {
                gl_Position = projMat * modelViewMat * pos;
            
                texPos = UV;
                vertexColor = color;
            }
            """
    );
    public final GlslFileEntry POSITION_TEXTURE_COLOR_FRAGMENT = CometLoaders.STRING.createGlslFileEntry(
            "shader.position-texture-color.fragment",
            """
            #version 330 core
            
            in vec2 texPos;
            in vec4 vertexColor;
            
            #include<shaderColor>
            uniform sampler2D u_Texture;
            
            out vec4 fragColor;
            
            void main() {
                fragColor = texture(u_Texture, texPos) * shaderColor * vertexColor;
            }
            """
    );
}
