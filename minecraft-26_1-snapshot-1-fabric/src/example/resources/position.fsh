#version 330 core

precision lowp float;

uniform vec4 shaderColor;

out vec4 fragColor;

void main() {
    fragColor = shaderColor;
}