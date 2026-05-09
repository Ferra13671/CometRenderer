#inouts {
    float param1;
    mat4 param2;
    vec4 param3;
    vec2 param4;
    uint param6;
};

void main() {
    param1 = param1_in * 2;
    param2 = param2_in;
    param3 = param3_in.yxwz;
    param4 = param4_in.yx;
    param6 = param6_in + 10;
}