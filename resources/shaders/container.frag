#version 400

out vec4 FragColor;

in vec2 UV;

uniform vec4 background;
uniform float width;
uniform float height;
uniform float radius;

void main() {
    vec2 position = vec2(UV.x * width, UV.y * height);
    vec2 distance = vec2(min(width - position.x, position.x), min(height - position.y, position.y));

    if (distance.x < radius && distance.y < radius) {
        distance = vec2(radius - distance.x, radius - distance.y);
        if (length(distance) > 16) {
            discard;
        }
    }

    FragColor = background;
}