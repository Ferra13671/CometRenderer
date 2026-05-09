#version auto

in vec4 position;
#inouts {
    vec2 texPos;
};
in vec2 UV;

#include<matrices>

void main() {
    gl_Position = projMat * modelViewMat * position;

    texPos = texPos_in;
}