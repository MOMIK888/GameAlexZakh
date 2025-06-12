attribute vec3 a_position;
attribute vec2 a_texCoord0;

uniform mat4 u_proj;
uniform mat4 u_view; // rotation only — camera view matrix without translation

varying vec2 v_texCoords;

void main() {
    v_texCoords = a_texCoord0;

    // Remove camera translation: use only rotation from view matrix
    mat4 viewRotOnly = mat4(mat3(u_view)); // strips translation

    gl_Position = u_proj * viewRotOnly * vec4(a_position, 1.0);
}

