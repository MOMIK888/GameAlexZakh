attribute vec3 a_position;
uniform mat4 u_projTrans;
varying vec3 v_texCoord;
precision mediump float;

void main() {
    v_texCoord = a_position;
    gl_Position = u_projTrans * vec4(a_position, 1.0);
}
