/*
If you add the BetterCompilerTags.GLSL_VERSION tag to the program registry, the version specified
 in the #version directive will be replaced with the one specified in the tag.
*/
#version 330 core

in vec4 position;

void main() {
    gl_Position = projMat * modelViewMat * position;
}