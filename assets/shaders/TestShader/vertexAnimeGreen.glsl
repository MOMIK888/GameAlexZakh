attribute vec3 a_position;
attribute vec3 a_normal; // Add normals
attribute vec2 a_texCoord0;
uniform mat4 u_projViewTrans;
uniform mat4 u_worldTrans;
varying vec2 v_texCoords;
varying vec3 v_normal;
varying vec3 v_localPos;

void main() {
    v_texCoords = a_texCoord0;
    v_localPos = a_position;
    v_normal = (u_worldTrans * vec4(a_normal, 0.0)).xyz; // Transform normals
    gl_Position = u_projViewTrans * u_worldTrans * vec4(a_position, 1.0);
}
