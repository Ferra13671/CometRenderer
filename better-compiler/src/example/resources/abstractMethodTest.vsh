#version 330 core

in vec4 position;

/*
The #abstractMethods directive specifies that the compiler should add content to the method.

The method before the directive must always follow the structure: $type $name($params).
*/
void main(); #abstractMethod