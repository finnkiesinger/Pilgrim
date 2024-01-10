#version 400

layout (location=0) in vec2 position;
layout (location=1) in vec2 uv;

out vec2 UV;

uniform mat4 projection;

void main() {
    gl_Position = projection * vec4(position, 0.0f, 1.0f);
    UV = uv;
}