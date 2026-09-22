package _3d;

import com.ferra13671.cometrenderer.CometLoader;
import com.ferra13671.cometrenderer.glsl.shader.GLShader;
import com.ferra13671.cometrenderer.glsl.shader.ShaderType;
import com.ferra13671.cometrenderer.glsl.uniform.UniformType;

public class Shaders {
    public final GLShader positionVertex = CometLoader.STRING.createShaderBuilder()
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
    public final GLShader positionFragment = CometLoader.STRING.createShaderBuilder()
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
    public final GLShader defaultMaterialVertex = CometLoader.STRING.createShaderBuilder()
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
            .build();
    public final GLShader defaultMaterialFragment = CometLoader.STRING.createShaderBuilder()
            .info(
                    "default_material_fragment",
                          """
                          #version 330 core
                          
                          precision lowp float;
                          
                          in vec4 vertexColor;
                          in vec2 texPos;
                          in vec3 normal;
                          in vec4 fragPosLightSpace;
                          
                          uniform float ambientLight;
                          uniform vec3 sunVector;
                          
                          uniform vec4 shaderColor;
                          uniform sampler2D u_Texture;
                          uniform sampler2D shadowMap;
                          
                          out vec4 fragColor;
                          
                          float calculateShadow(vec4 fragPosLightSpace, vec3 normal, vec3 lightDir) {
                              vec3 projCoords = fragPosLightSpace.xyz / fragPosLightSpace.w;
                              projCoords = projCoords * 0.5 + 0.5;

                              if (
                                  projCoords.x < 0.0 || projCoords.x > 1.0 ||
                                  projCoords.y < 0.0 || projCoords.y > 1.0 ||
                                  projCoords.z > 1.0
                              )
                                  return 0.0;
                          
                              float closestDepth = texture(shadowMap, projCoords.xy).r;
                              float currentDepth = projCoords.z;
                          
                              float bias = max(0.002 * (1.0 - dot(normal, lightDir)), 0.0001);
                          
                              float shadow = 0.0;
                              vec2 texelSize = 1.0 / textureSize(shadowMap, 0);
                              for (int x = -1; x <= 1; ++x) {
                                  for (int y = -1; y <= 1; ++y) {
                                      float pcfDepth = texture(shadowMap, projCoords.xy + vec2(x, y) * texelSize).r;
                                      shadow += currentDepth - bias > pcfDepth ? 1.0 : 0.0;
                                  }
                              }
                              shadow /= 9.0;
                          
                              return shadow;
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
            .build();
    public final GLShader shadowTextureFragment = CometLoader.STRING.createShaderBuilder()
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
