#version auto

in vec2 texPos;
in vec4 vertexColor;

#include<shaderColor>
uniform sampler2D u_Texture;

out vec4 fragColor;

void main() {
    fragColor = texture(u_Texture, texPos) * shaderColor * vertexColor;
}