//Liquid glass fragment shader (copied and modified from Shadertoy: https://www.shadertoy.com/view/wccSDf)
//Я ебал это говно

#include<rounded-base>

#inputs {
    vec4 vertexColor;
    vec2 offset;
    vec2 halfSize;
    float radius;
};

#uniforms {
    sampler2D u_Texture;
    vec2 texelFetch;
};

float thickness; #constant<default = 16.>
float index; #constant<default = 1.5>
float base_height; #constant<default = thickness * 8.>
float color_mix; #constant<default = 0.2>
vec3 color_base; #constant<default = vec3(1.)>

// Thickness is the t in the doc.
vec3 getNormal(float sd, float thickness) {
    float dx = dFdx(sd);
    float dy = dFdy(sd);

    // The cosine and sine between normal and the xy plane.
    float n_cos = max(thickness + sd, 0.) / thickness;
    float n_sin = sqrt(1. - n_cos * n_cos);

    return normalize(vec3(dx * n_cos, dy * n_cos, n_sin));
}

// The height (z component) of the pad surface at sd.
float height(float sd, float thickness) {
    if(sd >= 0.) {
        return 0.;
    }
    if(sd < -thickness) {
        return thickness;
    }

    float x = thickness + sd;
    return sqrt(thickness * thickness - x * x);
}

void main() {
    vec2 uv = gl_FragCoord.xy * texelFetch;

    float sd = roundedBoxSDF(offset, halfSize, radius);

    // Background pass-through with anti-aliasing
    vec4 bg_col = mix(vec4(0.), texture(u_Texture, uv), clamp(sd, 0.0, 1.0) * 0.1 + 0.9);
    bg_col.a = smoothstep(-2., 0., sd);

    vec3 normal = getNormal(sd, thickness);

    // A ray going -z hits the top of the pad, where would it hit on
    // the z = -base_height plane?
    vec3 incident = vec3(0., 0., -1.); // Should be normalized.
    vec3 refract_vec = refract(incident, normal, 1. / index);
    float h = height(sd, thickness);
    float refract_length = (h + base_height) / dot(vec3(0., 0., -1.), refract_vec);
    // This is the screen coord of the ray hitting the z = -base_height
    // plane.
    vec2 coord1 = gl_FragCoord.xy + refract_vec.xy * refract_length;
    vec3 refract_color = texture(u_Texture, coord1 * texelFetch).rgb;

    // Reflection
    float r = abs(sin((3.14 * 0.25) + atan(offset.x, offset.y)));
    vec3 reflect_color = vec3(clamp(r, 0.45, 1.));

    fragColor = vec4(mix(mix(refract_color, reflect_color, (1. - normal.z) * 2.), color_base, color_mix), 1.);


    fragColor = clamp(fragColor, 0., 1.) * vertexColor * shaderColor;
    bg_col = clamp(bg_col,0.,1.);
    fragColor = mix(fragColor, bg_col, bg_col.a);
    fragColor.a = 1. - smoothstep(0., edgeSoftness, sd);
}