#version 330 core

precision lowp float;

uniform sampler2D u_Texture;
uniform vec2 texelSize;

out vec4 fragColor;

const int quality = 1;
const vec4 color = vec4(1., 0., 0., 0.3);
const vec4 outlinecolor = vec4(1., 0., 0., 1.);

void main() {
    vec2 texCoord = gl_FragCoord.xy * texelSize;

    vec4 centerCol = texture(u_Texture, texCoord);

    int qualityInternal = quality;

    if(centerCol.a != 0) {
        fragColor = color;
    } else {
        for (int x = -qualityInternal; x < qualityInternal + 1; x++) {
            for (int y = -qualityInternal; y < qualityInternal + 1; y++) {
                vec2 offset = vec2(x, y);
                vec2 coord = texCoord + offset * texelSize;
                vec4 t = texture(u_Texture, coord);
                if (t.a != 0){
                    fragColor = outlinecolor;
                    return;
                }
            }
        }
        fragColor = vec4(0., 0., 0., 0.);
    }
}