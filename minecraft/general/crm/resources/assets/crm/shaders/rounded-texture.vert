#version auto

in vec4 pos;
in vec2 UV;
in vec4 color;
in vec2 _rectPosition;
in vec2 _halfSize;
in float _radius;

#include<matrices>

out vec2 texPos;
out vec4 vertexColor;
out vec2 rectPosition;
out vec2 halfSize;
out float radius;

void main() {
    gl_Position = projMat * modelViewMat * pos;
    texPos = UV;
    vertexColor = color;
    rectPosition = _rectPosition;
    halfSize = _halfSize;
    radius = _radius;
}