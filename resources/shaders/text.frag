#version 400

in vec2 UV;
out vec4 FragColor;

uniform sampler2D text;
uniform vec4 color;

void main() {
    FragColor = vec4(vec3(color), color.w * texture(text, UV).r);
}