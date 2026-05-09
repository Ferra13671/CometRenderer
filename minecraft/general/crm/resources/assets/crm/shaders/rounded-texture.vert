#version auto

#inputs {
    vec4 pos;
    vec2 UV;
    vec4 color;
    vec2 _rectPosition;
    vec2 _halfSize;
    float _radius;
};

#include<matrices>

#outputs {
    vec2 texPos;
    vec4 vertexColor;
    vec2 offset;
    vec2 halfSize;
    float radius;
};

void main() {
    gl_Position = projMat * modelViewMat * pos;
    texPos = UV;
    vertexColor = color;
    offset = pos.xy - _rectPosition;
    halfSize = _halfSize;
    radius = _radius;
}