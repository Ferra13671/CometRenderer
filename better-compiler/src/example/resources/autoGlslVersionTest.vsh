/*
'auto' means that if we have not added the BetterCompilerTags.GLSL_VERSION tag to the program registry,
 the version specified in the CometRenderer global registry will be inserted.
*/
#version auto

in vec4 position;

void main() {
    gl_Position = projMat * modelViewMat * position;
}