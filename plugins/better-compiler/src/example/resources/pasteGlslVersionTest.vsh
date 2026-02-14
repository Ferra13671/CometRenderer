/*
If the #version directive is missing from the shader, but you add the BetterCompilerTags.GLSL_VERSION
 tag to the program registry, the compiler will automatically insert a directive with the specified version into the shader.
*/

in vec4 position;

void main() {
    gl_Position = projMat * modelViewMat * position;
}