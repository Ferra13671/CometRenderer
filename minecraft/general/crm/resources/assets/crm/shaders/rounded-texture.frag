#version auto

precision lowp float;

in vec2 texPos;
in vec4 vertexColor;
in vec2 offset;
in vec2 halfSize;
in float radius;

#include<rounded, shaderColor>
uniform sampler2D u_Texture;

out vec4 fragColor;

float edgeSoftness; #constant<default = 2f>

void main() {
    float distance = roundedBoxSDF(offset, halfSize, radius);

    vec4 col = vertexColor * texture(u_Texture, texPos);

    fragColor = vec4(col.rgb, (1. - smoothstep(0., edgeSoftness, distance)) * col.a) * shaderColor;
}