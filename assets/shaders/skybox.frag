precision mediump float;

uniform sampler2D u_texture;
varying vec3 v_texCoord;

void main() {
    vec3 absCoord = abs(v_texCoord); // Get absolute values of the coordinates
    vec2 uv;

    // Determine which face to use based on the largest component
    if (absCoord.x > absCoord.y && absCoord.x > absCoord.z) {
        uv = v_texCoord.x > 0.0 ? v_texCoord.zy : v_texCoord.zy;
        uv.x = -uv.x; // Flip the texture for the correct orientation
    } else if (absCoord.y > absCoord.x && absCoord.y > absCoord.z) {
        uv = v_texCoord.y > 0.0 ? v_texCoord.xz : v_texCoord.xz;
        uv.y = -uv.y; // Flip the texture for the correct orientation
    } else {
        uv = v_texCoord.z > 0.0 ? v_texCoord.xy : v_texCoord.xy;
    }

    // Normalize the texture coordinates based on the size of the skybox mesh
    uv = (uv / 100.0 + 1.0) * 0.5; // Adjust 100.0 to match the size of your skybox mesh

    // Sample the texture
    gl_FragColor = texture2D(u_texture, uv);
}
