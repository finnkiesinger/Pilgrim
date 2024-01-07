#version 400

#define MAX_POINT_LIGHTS 2
#define MAX_DIRECTIONAL_LIGHTS 4
#define MAX_SPOT_LIGHTS 4

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

uniform PointLight pointLights[MAX_POINT_LIGHTS];
uniform DirectionalLight directionalLights[MAX_DIRECTIONAL_LIGHTS];

uniform int nrPointLights;
uniform int nrDirectionalLights;

uniform sampler2D texture_diffuse;
uniform sampler2D texture_specular;

uniform vec3 cameraPosition;

uniform int hasTexture;

void point(
    in PointLight pointLight,
    in vec3 norm,
    in vec3 viewDir,
    inout vec4 color,
    in vec4 matAmbient,
    in vec4 matDiffuse,
    in vec4 matSpecular
) {
    float distance = length(pointLight.position - FragPos);
    float attenuation = 1.0 / (pointLight.constant + pointLight.linear * distance + pointLight.quadratic * (distance * distance));


    vec3 lightDir = normalize(pointLight.position - FragPos);
    float D = max(dot(norm, lightDir), 0.0);

    vec4 ambient = attenuation * matAmbient * vec4(pointLight.ambient, 1.0f);
    vec4 diffuse = vec4(D * attenuation * vec3(matDiffuse), matDiffuse.w) * vec4(pointLight.diffuse, 1.0f);


    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    vec4 specular = spec * attenuation * matSpecular * vec4(pointLight.specular, 1.0f);

    color += (ambient + diffuse + specular);
}

void directional(
    in DirectionalLight directionalLight,
    in vec3 norm,
    in vec3 viewDir,
    inout vec4 color,
    in vec4 matAmbient,
    in vec4 matDiffuse,
    in vec4 matSpecular
) {
    vec3 lightDir = normalize(-directionalLight.direction);
    float D = max(dot(norm, lightDir), 0.0);

    vec4 ambient = matAmbient * vec4(directionalLight.ambient, 1.0f);
    vec4 diffuse = vec4(D * vec3(matDiffuse), matDiffuse.w) * vec4(directionalLight.diffuse, 1.0f);

    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    vec4 specular = spec * matSpecular * vec4(directionalLight.specular, 1.0f);

    color += (ambient + diffuse + specular);
}

void main() {
    vec3 norm = normalize(Normal);
    vec3 viewDir = normalize(cameraPosition - FragPos);

    vec4 result = vec4(0.0f);

    vec4 matAmbient;
    vec4 matDiffuse;
    vec4 matSpecular;

    if (hasTexture > 0) {
        matAmbient = texture(texture_diffuse, TexCoord);
        matDiffuse = texture(texture_diffuse, TexCoord);
        matSpecular = texture(texture_specular, TexCoord);
    } else {
        matAmbient = material.diffuse;
        matDiffuse = material.diffuse;
        matSpecular = material.specular;
    }

    for (int i = 0; i < nrPointLights; i++) {
        directional(directionalLights[i], norm, viewDir, result, matAmbient, matDiffuse, matSpecular);
    }

    for (int i = 0; i < nrDirectionalLights; i++) {
        point(pointLights[i], norm, viewDir, result, matAmbient, matDiffuse, matSpecular);
    }

    FragColor = result;
}