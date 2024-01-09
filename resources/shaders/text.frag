#version 400

in vec2 UV;
out vec4 FragColor;

uniform sampler2D text;

void main() {
    FragColor = vec4(1.0f, 1.0f, 1.0f, texture(text, UV).r);
}