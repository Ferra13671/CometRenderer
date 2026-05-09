#version auto

#inputs {
    vec4 pos;
    vec4 color;
    vec2 rectPosition;
    vec2 _halfSize;
    float _radius;
};

#include<matrices>

#outputs {
    vec4 vertexColor;
    vec2 offset;
    vec2 halfSize;
    float radius;
};

void main() {
    gl_Position = projMat * modelViewMat * pos;
    vertexColor = color;
    offset = pos.xy - rectPosition;
    halfSize = _halfSize;
    radius = _radius;
}