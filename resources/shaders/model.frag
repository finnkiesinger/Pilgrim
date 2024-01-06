#version 400

out vec4 FragColor;

in vec3 Normal;
in vec2 TexCoord;
in vec3 FragPos;

struct Material {
    vec4 diffuse;
    vec4 ambient;
    vec4 specular;
    float shininess;
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

uniform vec3 cameraPosition;

void main() {
    vec3 norm = normalize(Normal);
    vec3 lightDir = normalize(light.position - FragPos);
    float D = max(dot(norm, lightDir), 0.0);

    vec4 ambient = 0.1f * material.ambient * material.diffuse * light.color;
    vec4 diffuse = vec4(D * vec3(material.diffuse), material.diffuse.w) * light.color;

    float specularStrength = 1.0;
    vec3 viewDir = normalize(cameraPosition - FragPos);
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 100);
    vec4 specular = specularStrength * spec * material.specular * light.color;

    FragColor = ambient + diffuse + specular;
}