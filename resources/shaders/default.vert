#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec3 normals;

void main() {
    gl_Position = position;
}