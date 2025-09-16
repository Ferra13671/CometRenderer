#version 330 core

precision lowp float;

in vec4 vertexColor;

uniform vec4 color;

out vec4 fragColor;

void main() {
    fragColor = color * vertexColor;
}