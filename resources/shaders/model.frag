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
    vec4 emitting;
    float shininess;
};

struct PointLight {
    vec3 position;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;

    float constant;
    float linear;
    float quadratic;
};

struct DirectionalLight {
    vec3 direction;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

struct SpotLight {
    vec3 direction;

    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

uniform Material material;

uniform PointLight pointLight;
uniform DirectionalLight directionalLight;

uniform sampler2D texture_diffuse;
uniform sampler2D texture_specular;

uniform vec3 cameraPosition;

void point(out vec4 color) {
    float distance = length(pointLight.position - FragPos);
    float attenuation = 1.0 / (pointLight.constant + pointLight.linear * distance + pointLight.quadratic * (distance * distance));

    vec3 norm = normalize(Normal);
    vec3 lightDir = normalize(pointLight.position - FragPos);
    float D = max(dot(norm, lightDir), 0.0);

    vec4 ambient = attenuation * material.diffuse * vec4(pointLight.ambient, 1.0f);
    vec4 diffuse = vec4(D * attenuation * vec3(material.diffuse), material.diffuse.w) * vec4(pointLight.diffuse, 1.0f);

    vec3 viewDir = normalize(cameraPosition - FragPos);
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    vec4 specular = spec * material.specular * vec4(pointLight.specular, 1.0f);

    color = ambient + diffuse + specular;
}

void directional(out vec4 color) {
    vec3 norm = normalize(Normal);
    vec3 lightDir = normalize(-directionalLight.direction);
    float D = max(dot(norm, lightDir), 0.0);

    vec4 ambient = material.diffuse * vec4(directionalLight.ambient, 1.0f);
    vec4 diffuse = vec4(D * vec3(material.diffuse), material.diffuse.w) * vec4(directionalLight.diffuse, 1.0f);

    vec3 viewDir = normalize(cameraPosition - FragPos);
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    vec4 specular = spec * material.specular * vec4(directionalLight.specular, 1.0f);

    color = ambient + diffuse + specular;
}

void textured(out vec4 color) {

}

void main() {
    vec4 directionalLight = vec4(0.0f, 0.0f, 0.0f, 0.0f);
    directional(directionalLight);

    vec4 pointLight = vec4(0.0f, 0.0f, 0.0f, 0.0f);
    point(pointLight);

    FragColor = directionalLight + pointLight;
}