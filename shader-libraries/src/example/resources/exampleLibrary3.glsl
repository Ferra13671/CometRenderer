//This code was taken from the "exampleLib3" library. If you see this code after "exampleLib2" library code, multy inject test has been passed.
float roundedBoxSDF(vec2 centerPosition, vec2 size, float radius) {
    return length(max(abs(centerPosition) - size + radius, 0.)) - radius;
}