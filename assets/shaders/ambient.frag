#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;
uniform float u_ambientLight; // Ambient light intensity (e.g., 0.5 for half brightness)

varying vec2 v_texCoords;

void main() {
    // Sample the texture
    vec4 texColor = texture2D(u_texture, v_texCoords);

    // Apply ambient light
    texColor.rgb *= u_ambientLight;

    // Output the final color
    gl_FragColor = texColor;
}
