#version 330

layout (location = 0) in vec2 position;

out vec2 texCoord;

uniform mat4 modelToClipMatrix;

void main() {

    gl_Position = modelToClipMatrix * vec4(position, 0, 1);

    texCoord = position;
}