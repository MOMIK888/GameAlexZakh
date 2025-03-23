#version 300 es
precision mediump float;

in vec2 vTexCoord;
in vec3 vNormal;

out vec4 FragColor;

// Material properties
uniform vec3 uBaseColor;
uniform float uMetallic;
uniform float uRoughness;
uniform float uAlpha;
void main()
{
    vec4 baseColor = vec4(uBaseColor, uAlpha);

    // Basic lighting calculation (example)
    vec3 lightDir = normalize(vec3(1.0, 1.0, 1.0));
    float diff = max(dot(normalize(vNormal), lightDir), 0.0);
    vec3 lighting = vec3(0.1) + diff * vec3(0.9);

    FragColor = vec4(baseColor.rgb * lighting, baseColor.a);
}
