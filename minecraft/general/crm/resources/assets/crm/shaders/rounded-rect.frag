#version auto

precision lowp float;

in vec4 vertexColor;
in vec2 rectPosition;
in vec2 halfSize;
in float radius;

#include<rounded, shaderColor>
uniform float height;

out vec4 fragColor;

float edgeSoftness; #constant<default = 2f>

void main() {
    vec2 _position = vec2(rectPosition.x, height - rectPosition.y);

    float distance = roundedBoxSDF(gl_FragCoord.xy - _position, halfSize, radius);

    fragColor = vec4(vertexColor.rgb, (1. - smoothstep(0., edgeSoftness, distance)) * vertexColor.a) * shaderColor;
}