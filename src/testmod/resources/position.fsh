#version 330 core

precision lowp float;

uniform vec4 color;

out vec4 fragColor;

void main() {
    fragColor = color;
}