#version auto

precision lowp float;

in vec4 vertexColor;
in vec2 offset;
in vec2 halfSize;
in float radius;

#include<rounded, shaderColor>

out vec4 fragColor;

float edgeSoftness; #constant<default = 2f>

void main() {
    float distance = roundedBoxSDF(offset, halfSize, radius);

    fragColor = vec4(vertexColor.rgb, (1. - smoothstep(0., edgeSoftness, distance)) * vertexColor.a) * shaderColor;
}