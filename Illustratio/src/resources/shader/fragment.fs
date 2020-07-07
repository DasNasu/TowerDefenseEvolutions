#version 330

in vec2 outTexCoord;

out vec4 fragColor;

uniform sampler2D textureSampler;

void main() {
	vec4 textureColor = texture(textureSampler, outTexCoord);
 	if(textureColor.a < 0.5) discard;
	fragColor = textureColor;
}