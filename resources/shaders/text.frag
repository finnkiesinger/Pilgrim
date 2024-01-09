#version 400

in vec2 UV;
out vec4 FragColor;

uniform sampler2D text;

void main() {
    FragColor = texture(text, UV);
}