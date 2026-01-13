#version 330 core

in vec4 position;

//Lib section
#include<exampleLib1>
#include<exampleLib2, exampleLib3>
//---------//

void main() {
    gl_Position = projMat * modelViewMat * position;
}