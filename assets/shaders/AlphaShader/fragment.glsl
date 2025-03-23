#ifdef GL_ES
precision mediump float; // Use medium precision for better performance on mobile
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;

void main() {
    vec4 textureColor = texture2D(u_texture, v_texCoords);
    vec4 finalColor = v_color * textureColor;
    gl_FragColor = finalColor;
    if (gl_FragColor.a <= 0.0) {
        discard;
    }
}
