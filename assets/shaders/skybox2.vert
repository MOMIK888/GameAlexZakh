attribute vec3 a_position;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;
uniform vec3 u_offset; // New: Translation offset

varying vec2 v_texCoords;

void main() {
    v_texCoords = a_texCoord0;
    gl_Position = u_projTrans * vec4(a_position + u_offset, 1.0); // Apply translation
}
