#version auto

in vec4 pos;
#inouts {
    vec2 texPos;
    vec4 vertexColor;
};

#include<matrices>

void main() {
    gl_Position = projMat * modelViewMat * pos;

    texPos = texPos_in;
    vertexColor = vertexColor_in;
}