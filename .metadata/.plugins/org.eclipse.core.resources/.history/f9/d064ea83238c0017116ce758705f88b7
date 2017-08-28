#version 400 core

//in vec3 color;
in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float visibility;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;

// diffuse lighting is shadowing
// specular lighting is shinning
void main(void) { 

	/* Normalize vectors */
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);

	/* Diffuse lighting (shadowing) with Ambient lighting */ 
	float nDotl = dot(unitNormal, unitLightVector); // 1 if parallel, 0 if perpindicular
	float brightness = max(nDotl, 0.2); // sometimes dot product returns less than 0 // 0.2 for ambient lighting so no spots are all black
	vec3 diffuse = brightness * lightColor;

	/* Reflection */
	vec3 unitVectorToCamera = normalize(toCameraVector);
	vec3 lightDirection = -unitLightVector;
	vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
	
	/* Specular light (shinning) */
	float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
	specularFactor = max(specularFactor, 0.0f);
	float dampedFactor = pow(specularFactor, shineDamper); // makes lower specular light lower, but higher does not change much
	vec3 finalSpecular = dampedFactor * reflectivity * lightColor;

	/* Texture */
	vec4 textureColor = texture(textureSampler, pass_textureCoords);

	/* Discards transparent black textures, such as grass */
	if (textureColor.a < 0.5){
		discard;
	}

	/* Final color of pixel */
	out_Color = vec4(diffuse,1.0) * textureColor + vec4(finalSpecular, 1.0f);
	
	/* Fog */
	out_Color = mix(vec4(skyColor, 1.0f), out_Color, visibility);

}