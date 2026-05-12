#version 150

/*#moj_import <fog.glsl>

uniform sampler2D Sampler0;
uniform sampler2D Sampler1; // required but unused
uniform sampler2D Sampler2; // required but unused

uniform float u_Time;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;
uniform int FogShape;

in float vertexDistance;
in vec4 vertexColor;
in vec4 lightMapColor;
in vec4 overlayColor;
in vec2 texCoord0;

out vec4 fragColor;*/

void main() {
    /*vec4 color = texture(Sampler0, texCoord0);

    if (color.a < 0.1) {
        discard;
    }

    // Vanilla entity translucent pipeline
    color *= vertexColor * ColorModulator;
    color.rgb = mix(overlayColor.rgb, color.rgb, overlayColor.a);
    color *= lightMapColor;

    // Shimmer
    float shimmer = 0.15 * sin(u_Time * 2.0 + texCoord0.y * 12.0);
    color.a *= (0.85 + shimmer);

    // Fog (correct signature)
    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);*/
}