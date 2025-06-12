#ifdef GL_ES
precision mediump float;
#endif
varying vec2 v_texCoord;
uniform sampler2D u_shadowMap;

void main() {
    float depth = texture2D(u_shadowMap, v_texCoord).r;
    gl_FragColor = vec4(vec3(depth), 1.0); // grayscale depth
}
