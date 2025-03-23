#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoords;
varying vec3 v_normal;
varying vec3 v_localPos;
uniform float u_colorRampPosition;
uniform vec3 u_color1;
uniform vec3 u_color2;
uniform vec3 u_lightDir; // Light direction

void main() {
    // Color ramp based on local position
    float t = step(u_colorRampPosition, v_localPos.x);
    vec3 baseColor = mix(u_color1, u_color2, t);

    // Sharp transition between lit and shadowed areas
    vec3 normal = normalize(v_normal);
    float diff = dot(normal, u_lightDir);
    vec3 finalColor = (diff > 0.0) ? baseColor : u_color2;

    gl_FragColor = vec4(finalColor, 1.0);
}
