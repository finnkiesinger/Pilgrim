#version 330

in vec2 TexCoords;

out vec4 FragColor;

uniform sampler2D texture1;
uniform int hovered;

void main() {
    if (hovered > 0) {
        FragColor = texture(texture1, TexCoords) * vec4(1.0f, 0.5f, 0.5f, 1.0f);
    } else {
        FragColor = texture(texture1, TexCoords);
    }
}