#version auto

in vec4 pos;
in vec4 color;

#include<matrices>

out vec4 vertexColor;

void main() {
    gl_Position = projMat * modelViewMat * pos;

    vertexColor = color;
}