#version 400

out vec4 FragColor;

in vec3 Normal;
in vec2 TexCoord;

struct Material {
    vec4 diffuse;
};

uniform Material material;

uniform sampler2D texture_diffuse;
uniform sampler2D texture_specular;

void main() {
    FragColor = material.diffuse;
}