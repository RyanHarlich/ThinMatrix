#version 400 core

in vec2 position;

out vec4 clipSpace;
out vec2 textureCoords;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

const float tiling = 6.0f;


void main(void) {

	clipSpace = projectionMatrix * viewMatrix * modelMatrix * vec4(position.x, 0.0, position.y, 1.0);
	gl_Position = clipSpace;
 	textureCoords = vec2(position.x / 2.0f + 0.5f, position.y / 2.0f + 0.5f) * tiling;
}