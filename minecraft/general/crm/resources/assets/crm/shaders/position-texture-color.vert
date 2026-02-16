#version auto

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