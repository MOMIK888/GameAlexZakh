attribute vec3 a_position;
attribute vec2 a_texCoord0;

uniform mat4 u_worldTrans;
uniform mat4 u_projViewTrans;
uniform vec2 u_uvOffset;

varying vec2 v_texCoord;

void main() {
    v_texCoord = a_texCoord0 + u_uvOffset;
    gl_Position = u_projViewTrans * u_worldTrans * vec4(a_position, 1.0);
}
