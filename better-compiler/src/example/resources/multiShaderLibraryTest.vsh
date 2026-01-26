#version 330 core

in vec4 position;

#include<exampleLib2, exampleLib3>

void main() {
    gl_Position = projMat * modelViewMat * position;
}