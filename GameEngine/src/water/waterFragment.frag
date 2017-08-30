#version 400 core

in vec4 clipSpace;

out vec4 out_Color;

uniform sampler2D reflectionTexture; // above water
uniform sampler2D refractionTexture; // under water

void main(void) {

	vec2 ndc = (clipSpace.xy/clipSpace.w) / 2.0f + 0.5f; //normalizedDeviceCoordinates
	vec2 refractTexCoords = vec2(ndc.x, ndc.y);
	vec2 reflectTexCoords = vec2(ndc.x, -ndc.y);
	
	vec4 reflectColor = texture(reflectionTexture, reflectTexCoords);
	vec4 refractColor = texture(refractionTexture, refractTexCoords);
	

	out_Color = mix(reflectColor, refractColor, 0.5);

}