#version auto

precision lowp float;

#include<shaderColor>

out vec4 fragColor;

void main() {
    fragColor = shaderColor;
}