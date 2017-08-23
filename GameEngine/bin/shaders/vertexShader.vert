#version 400 core

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

//out vec3 color;
out vec2 pass_textureCoords;
out vec3 surfaceNormal;
out vec3 toLightVector;
out vec3 toCameraVector;
out float visibility;

uniform mat4 transformationMatrix; 	//rotation and translation and matrix
uniform mat4 projectionMatrix;		//the frustum
uniform mat4 viewMatrix;			// position in respect to the world
uniform vec3 lightPosition;

uniform float useFakeLighting;

const float density = 0.007f; //= 0.0035f; out in the distance fog // the amount of fog
const float gradient = 1.5f; // =5.0f; out in the distance fog// how quickly visibility decreases with distance

void main(void) {

	vec4 worldPosition = transformationMatrix * vec4(position, 1.0f);
	vec4 positionRelativeToCam = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * positionRelativeToCam;
	pass_textureCoords = textureCoords;

	vec3 actualNormal = normal;
	if (useFakeLighting > 0.5) {
		actualNormal = vec3(0.0f, 1.0f, 0.0f); // points directly up, for grass lighting
	}
	
	surfaceNormal = (transformationMatrix * vec4(actualNormal, 0.0f)).xyz;
	toLightVector = lightPosition - worldPosition.xyz;
	toCameraVector = (inverse(viewMatrix) * vec4(0.0f, 0.0f, 0.0f, 1.0f)).xyz - worldPosition.xyz;

	float distance = length(positionRelativeToCam.xyz);
	visibility = exp(-pow((distance*density), gradient)); // fog forumla
	visibility = clamp(visibility, 0.0f, 1.0f);
}