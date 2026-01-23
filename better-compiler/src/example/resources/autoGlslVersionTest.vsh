#version auto

in vec4 position;

void main() {
    gl_Position = projMat * modelViewMat * position;
}