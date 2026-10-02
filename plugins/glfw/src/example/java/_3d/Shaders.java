package _3d;

import com.ferra13671.cometrenderer.glsl.GLSLLoader;
import com.ferra13671.cometrenderer.glsl.shader.GLShader;
import com.ferra13671.cometrenderer.glsl.shader.ShaderType;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;

public class Shaders {
    public final GLShader positionVertex = GLShader.builder(GLSLLoader.STRING)
            .info(
                    "position_vertex",
                    """
                    #version 330 core
                    
                    in vec4 position;
                    
                    uniform mat4 projection;
                    uniform mat4 view;
                    
                    void main() {
                        gl_Position = projection * view * position;
                    }
                    """,
                    ShaderType.Vertex
            )
            .uniform("projection", UniformType.MATRIX4)
            .uniform("view", UniformType.MATRIX4)
            .build();
    public final GLShader positionFragment = GLShader.builder(GLSLLoader.STRING)
            .info(
                    "position_fragment",
                    """
                    #version 330 core
                    
                    precision lowp float;
                    
                    uniform vec4 shaderColor;
                    
                    out vec4 fragColor;
                    
                    void main() {
                        fragColor = shaderColor;
                    }
                    """,
                    ShaderType.Fragment
            )
            .build();
    public final GLShader defaultMaterialVertex = GLShader.builder(GLSLLoader.STRING)
            .info(
                    "default_material_vertex",
                    """
                            #version 330 core
                            
                            in vec4 position;
                            in vec4 vertexColor_in;
                            in vec2 texPos_in;
                            in vec3 normal_in;
                            
                            uniform mat4 projection;
                            uniform mat4 view;
                            uniform mat4 lightSpaceMatrix;
                            
                            out vec4 vertexColor;
                            out vec2 texPos;
                            out vec3 normal;
                            out vec4 fragPosLightSpace;
                            
                            void main() {
                                gl_Position = projection * view * position;
                                vertexColor = vertexColor_in;
                                texPos = texPos_in;
                                normal = normal_in;
                                fragPosLightSpace = lightSpaceMatrix * position;
                            }
                            """,
                    ShaderType.Vertex
            )
            .uniform("projection", UniformType.MATRIX4)
            .uniform("view", UniformType.MATRIX4)
            .uniform("lightSpaceMatrix", UniformType.MATRIX4)
            .build();
    public final GLShader defaultMaterialFragment = GLShader.builder(GLSLLoader.STRING)
            .info(
                    "default_material_fragment",
                          """
                          #version 330 core
                          
                          precision highp float;
                          
                          in vec4 vertexColor;
                          in vec2 texPos;
                          in vec3 normal;
                          in vec4 fragPosLightSpace;
                          
                          uniform float ambientLight;
                          uniform vec3 sunVector;
                          
                          uniform vec4 shaderColor;
                          uniform sampler2D u_Texture;
                          uniform sampler2DShadow shadowMap;
                          uniform vec2 shadowTexelSize;
                          uniform float compression;
                          
                          out vec4 fragColor;
                          
                          vec3 distortLightNDC(vec3 ndc) {
                              float r = length(ndc.xy);
                              if (r < 0.0001) return ndc;
                          
                              float newR = compression * r / (1.0 + (compression - 1.0) * r);
                          
                              return vec3(ndc.xy / r * newR, ndc.z);
                          }
                          
                          float calculateShadow(vec4 L, vec3 normal, vec3 lightDir) {
                              vec3 ndc = L.xyz / L.w;
                          
                              if (any(greaterThan(abs(ndc), vec3(1.0)))) return 0.;
                          
                              vec3 dndc = distortLightNDC(ndc);
                          
                              vec2 uv = dndc.xy * 0.5 + 0.5;
                              float depth = dndc.z * 0.5 + 0.5;
                          
                              float bias = max(0.002 * (1.0 - dot(normal, lightDir)), 0.0001);
                              depth -= bias;
                          
                              return 1.0 - texture(shadowMap, vec3(uv, depth));
                          }
                          
                          void main() {
                              vec3 normalizedSun = normalize(-sunVector);
                              vec3 normalizedNormal = normalize(normal);
                          
                              float shadow = calculateShadow(fragPosLightSpace, normalizedNormal, normalizedSun);
                              vec3 lit = (vec3(1.) - shadow);
                              vec4 light = max(vec4(lit, 1.) * dot(normalizedSun, normalizedNormal), ambientLight);
                          
                              fragColor = vertexColor * shaderColor * texture(u_Texture, texPos) * light;
                          }
                          """,
                    ShaderType.Fragment
            )
            .sampler("u_Texture")
            .sampler("shadowMap")
            .uniform("ambientLight", UniformType.FLOAT)
            .uniform("sunVector", UniformType.VEC3)
            .uniform("compression", UniformType.FLOAT)
            .build();
    public final GLShader defaultMaterialShadowVertex = GLShader.builder(GLSLLoader.STRING)
            .info(
                    "default_material_vertex",
                    """
                            #version 330 core
                            
                            in vec4 position;
                            in vec4 vertexColor_in;
                            in vec2 texPos_in;
                            in vec3 normal_in;
                            
                            uniform mat4 lightSpaceMatrix;
                            uniform float compression;
                            
                            out vec4 vertexColor;
                            out vec2 texPos;
                            out vec3 normal;
                            out vec4 fragPosLightSpace;
                            
                            vec3 distortLightNDC(vec3 ndc) {
                                float r = length(ndc.xy);
                                if (r < 0.0001) return ndc;
                            
                                float newR = compression * r / (1.0 + (compression - 1.0) * r);
                            
                                return vec3(ndc.xy / r * newR, ndc.z);
                            }
                            
                            void main() {
                                vec4 L = lightSpaceMatrix * position;
                                vec3 ndc = L.xyz / L.w;
                                vec3 dndc = distortLightNDC(ndc);
                            
                                gl_Position = vec4(dndc, 1.0);
                            
                                vertexColor = vertexColor_in;
                                texPos = texPos_in;
                                normal = normal_in;
                                fragPosLightSpace = L;
                            }
                            """,
                    ShaderType.Vertex
            )
            .uniform("lightSpaceMatrix", UniformType.MATRIX4)
            .uniform("compression", UniformType.FLOAT)
            .build();
    public final GLShader shadowTextureFragment = GLShader.builder(GLSLLoader.STRING)
            .info(
                    "shadow_texture_fragment",
                    """
                    #version 330 core
                    
                    void main() {}
                    """,
                    ShaderType.Fragment
            )
            .build();
}
