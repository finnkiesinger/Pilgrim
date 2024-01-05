#version 400

out vec4 FragColor;

in vec3 Normal;
in vec2 TexCoord;
in vec3 FragPos;

struct Material {
    vec4 diffuse;
    vec4 ambient;
};

struct Light {
    vec4 color;
    vec3 position;
    float intensity;
};

uniform Material material;
uniform Light light;

uniform sampler2D texture_diffuse;
uniform sampler2D texture_specular;

void main() {
    vec3 norm = normalize(Normal);
    vec3 lightDir = normalize(light.position - FragPos);
    float D = max(dot(norm, lightDir), 0.0);

    vec4 ambient = 0.1 * material.ambient * light.color;
    vec4 diffuse = vec4(D * vec3(material.diffuse), material.diffuse.w);
    FragColor = ambient + diffuse;
}