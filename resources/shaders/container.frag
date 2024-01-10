#version 400

out vec4 FragColor;

in vec2 UV;

uniform vec4 background;
uniform float width;
uniform float height;
uniform float radius;

void main() {
    FragColor = background;
}