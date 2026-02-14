#version 330 core

in vec4 position;

/*
The #constant directive means that the compiler should add
 a value to the field before the directive, and also add the 'const' keyword to the field.

The field before the directive must always follow the structure: $type $name.

You can also add a default value to a directive (by adding <default = $value> after the directive), which will be set for the field if you do not specify a value in the program's registry.
*/
float value; #constant

void main() {
    gl_Position = projMat * modelViewMat * position;
}