#version auto

precision lowp float;

in vec4 vertexColor;

#include<shaderColor>

out vec4 fragColor;

void main() {
    fragColor = shaderColor * vertexColor;
}