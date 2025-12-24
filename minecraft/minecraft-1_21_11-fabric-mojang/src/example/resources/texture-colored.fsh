#version 330 core

in vec2 texCoord;
in vec4 vertexColor;

uniform sampler2D u_Texture;
uniform vec4 shaderColor;

out vec4 fragColor;

void main() {
    fragColor = texture(u_Texture, texCoord) * shaderColor * vertexColor;
}