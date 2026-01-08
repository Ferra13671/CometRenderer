#version 330 core

precision lowp float;

in vec4 vertexColor;

uniform vec4 shaderColor;

out vec4 fragColor;

void main() {
    fragColor = shaderColor * vertexColor;
}