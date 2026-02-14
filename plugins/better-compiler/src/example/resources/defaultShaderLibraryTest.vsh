#version 330 core

in vec4 position;

#include<exampleLib1>

void main() {
    gl_Position = projMat * modelViewMat * position;
}