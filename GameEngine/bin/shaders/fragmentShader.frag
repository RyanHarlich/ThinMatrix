#version 400 core

//in vec3 color;
in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector[4];
in vec3 toCameraVector;
in float visibility;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3 lightColor[4];
uniform vec3 attenuation[4];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;

const float levels = 3.0f;

// diffuse lighting is shadowing
// specular lighting is shinning
void main(void) { 

	/* Normalize vectors */
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitVectorToCamera = normalize(toCameraVector);


	vec3 totalDiffuse = vec3(0.0f);
	vec3 totalSpecular = vec3(0.0f);

	for (int i = 0; i < 4; ++i) {
		/* Attenuated light brightness (fading light) */
		float distance = length(toLightVector[i]);
		float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
	
		/* Normalize vectors cont. */
		vec3 unitLightVector = normalize(toLightVector[i]);
	
		/* Diffuse lighting (shadowing) */ 
		float nDotl = dot(unitNormal, unitLightVector); // 1 if parallel, 0 if perpindicular
		float brightness = max(nDotl, 0.0); // sometimes dot product returns less than 0 // 0.2 for ambient lighting so no spots are all black // moved down below since this would add up multiple times, in totalDiffuse		
		/* Cel Shading */
		//float level = floor(brightness * levels);
		//brightness = level / levels;	
		/* Diffuse lighting (shadowing) continue */ 
		totalDiffuse = totalDiffuse + (brightness * lightColor[i]) / attFactor;	
	
		/* Reflection */
		vec3 lightDirection = -unitLightVector;
		vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
		
		/* Specular light (shinning) */
		float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
		specularFactor = max(specularFactor, 0.0f);
		float dampedFactor = pow(specularFactor, shineDamper); // makes lower specular light lower, but higher does not change much		
		/* Cel Shading */
		//level = floor(dampedFactor * levels);
		//dampedFactor = level / levels;		
		/* Specular light (shinning) continue */
		totalSpecular = totalSpecular +  (dampedFactor * reflectivity * lightColor[i]) / attFactor;
	}
	
	/* Ambient lighting */
	totalDiffuse = max(totalDiffuse, 0.2); // sometimes dot product returns less than 0 // 0.2 for ambient lighting so no spots are all black

	/* Texture */
	vec4 textureColor = texture(textureSampler, pass_textureCoords);

	/* Discards transparent black textures, such as grass */
	if (textureColor.a < 0.5){
		discard;
	}

	/* Final color of pixel */
	out_Color = vec4(totalDiffuse,1.0) * textureColor + vec4(totalSpecular, 1.0f);
	
	/* Fog */
	out_Color = mix(vec4(skyColor, 1.0f), out_Color, visibility);

}