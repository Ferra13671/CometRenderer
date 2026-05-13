#version auto

uniform sampler2D u_Texture;
#include<shaderColor>

out vec4 fragColor;

void main() {
    fragColor = vec4(texelFetch(u_Texture, ivec2(gl_FragCoord.xy), 0).rgb, 1.) * shaderColor;
}