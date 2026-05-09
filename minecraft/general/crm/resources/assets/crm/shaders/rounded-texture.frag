#version auto

precision lowp float;

#inputs {
    vec2 texPos;
    vec4 vertexColor;
    vec2 offset;
    vec2 halfSize;
    float radius;
};

#include<rounded, shaderColor>
uniform sampler2D u_Texture;

out vec4 fragColor;

float edgeSoftness; #constant<default = 2f>

void main() {
    float distance = roundedBoxSDF(offset, halfSize, radius);

    vec4 col = vertexColor * texture(u_Texture, texPos);

    fragColor = vec4(col.rgb, (1. - smoothstep(0., edgeSoftness, distance)) * col.a) * shaderColor;
}