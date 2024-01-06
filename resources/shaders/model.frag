#version 400

#define MAX_POINT_LIGHTS 10
#define MAX_DIRECTIONAL_LIGHTS 10
#define MAX_SPOT_LIGHTS 10

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

struct PointLight {
    vec4 color;
    vec3 position;

    float constant;
    float linear;
    float quadratic;
};

struct DirectionalLight {
    vec3 direction;
    vec3 color;
};

uniform Material material;
uniform PointLight pointLight;
uniform DirectionalLight directionalLight;

uniform sampler2D texture_diffuse;
uniform sampler2D texture_specular;

uniform vec3 cameraPosition;

void untextured(out vec4 color) {
    vec3 norm = normalize(Normal);
    vec3 lightDir = normalize(pointLight.position - FragPos);
    float D = max(dot(norm, lightDir), 0.0);

    vec4 ambient = 0.1f * material.ambient  * pointLight.color;
    vec4 diffuse = vec4(D * vec3(material.diffuse), material.diffuse.w) * pointLight.color;

    float specularStrength = 1.0;
    vec3 viewDir = normalize(cameraPosition - FragPos);
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    vec4 specular = specularStrength * spec * material.specular * pointLight.color;

    color = ambient + diffuse + specular;
}

void textured(out vec4 color) {

}

void main() {
    vec4 color = vec4(0.0f, 0.0f, 0.0f, 0.0f);

    untextured(color);

    FragColor = color;
}