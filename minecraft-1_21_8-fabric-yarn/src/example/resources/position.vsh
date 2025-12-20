#version 330 core

in vec4 position;

layout(std140) uniform Projection {
    mat4 projMat;
};
uniform mat4 modelViewMat;

void main() {
    gl_Position = projMat * modelViewMat * position;
}