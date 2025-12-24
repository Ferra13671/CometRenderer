#version 330 core

in vec4 pos;
in vec4 _vertexColor;

layout(std140) uniform Projection {
    mat4 projMat;
};
uniform mat4 modelViewMat;

out vec4 vertexColor;

void main() {
    gl_Position = projMat * modelViewMat * pos;

    vertexColor = _vertexColor;
}