#version auto

precision lowp float;

#include<rounded, shaderColor>

out vec4 fragColor;

float edgeSoftness; #constant<default = 2f>