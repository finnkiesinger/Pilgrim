#version 400

out vec4 FragColor;

in vec3 Normal;
in vec2 TexCoord;

uniform sampler2D texture_diffuse;
uniform sampler2D texture_specular;

void main() {
    FragColor = vec3(1.0f, 1.0f, 1.0f, 1.0f);
}