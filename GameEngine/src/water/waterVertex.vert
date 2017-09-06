#version 400 core

in vec2 position;

out vec4 clipSpace;
out vec2 textureCoords;
out vec3 toCameraVector;
out vec3 fromLightVector; // from the light to the water

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform vec3 cameraPosition;
uniform vec3 lightPosition;

const float tiling = 4.0f;


void main(void) {

	/* Projective Texture Mapping & Clipping Planes */
	clipSpace = projectionMatrix * viewMatrix * modelMatrix * vec4(position.x, 0.0, position.y, 1.0);
	gl_Position = clipSpace;
	
	/* DuDv Maps */
 	textureCoords = vec2(position.x / 2.0f + 0.5f, position.y / 2.0f + 0.5f) * tiling;
 	
 	/* Fresnel Effect */
 	vec4 worldPosition = modelMatrix * vec4(position.x, 0.0f, position.y, 1.0f);
 	toCameraVector = cameraPosition - worldPosition.xyz;
 	
 	/* Normal Map */
 	fromLightVector = worldPosition.xyz - lightPosition;
}