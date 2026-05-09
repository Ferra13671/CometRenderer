#version auto

in vec4 pos;
#inouts {
    vec4 vertexColor;
};

#include<matrices>

void main() {
    gl_Position = projMat * modelViewMat * pos;

    vertexColor = vertexColor_in;
}