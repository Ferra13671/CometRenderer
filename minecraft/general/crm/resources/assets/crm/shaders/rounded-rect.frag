#include<rounded-base>

#inputs {
    vec4 vertexColor;
    vec2 offset;
    vec2 halfSize;
    float radius;
};

void main() {
    float distance = roundedBoxSDF(offset, halfSize, radius);

    fragColor = vec4(vertexColor.rgb, (1. - smoothstep(0., edgeSoftness, distance)) * vertexColor.a) * shaderColor;
}