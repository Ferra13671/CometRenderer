#include<rounded-base>

#inputs {
    vec2 offset;
    vec2 halfSize;
    float radius;
};

uniform sampler2D u_Texture;

void main() {
    float distance = roundedBoxSDF(offset, halfSize, radius);

    fragColor = vec4(texelFetch(u_Texture, ivec2(gl_FragCoord.xy), 0).rgb, 1. - smoothstep(0., edgeSoftness, distance)) * shaderColor;
}