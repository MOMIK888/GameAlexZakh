attribute vec4 a_position;  // Vertex position
attribute vec4 a_color;     // Vertex color
attribute vec2 a_texCoord0; // Texture coordinates

varying vec4 v_color;       // Pass vertex color to fragment shader
varying vec2 v_texCoords;   // Pass texture coordinates to fragment shader

uniform mat4 u_projTrans;   // Projection matrix

void main() {
    v_color = a_color;
    v_texCoords = a_texCoord0;
    gl_Position = u_projTrans * a_position; // Transform vertex position
}
