#inouts {
    float param1;
    mat4 param2;
    vec4 param3;
    vec2 param4;
    atomic_uint param5;
    uint param6;
};

void main() {
    param1_out = param1_in * 2;
    param2_out = param2_in;
    param3_out = param3_in.yxwz;
    param4_out = param4_in.yx;
    param5_out = param5_in;
    param6_out = param6_in + 10;
}