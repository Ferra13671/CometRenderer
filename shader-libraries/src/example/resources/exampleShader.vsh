#version 330 core

in vec4 position;

#include<exampleLib>

void main() {
    gl_Position = projMat * modelViewMat * position;
}