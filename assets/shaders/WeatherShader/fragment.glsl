#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;

uniform float u_stretchUVX;
uniform float u_stretchUVY;
uniform float u_opacity;

uniform vec2 u_uvOffset;
uniform vec2 u_uvScale;

varying vec2 v_uv;

void main() {
    // Per-axis tiled UVs
    vec2 tiledUV;
    tiledUV.x = fract(v_uv.x * u_stretchUVX);
    tiledUV.y = fract(v_uv.y * u_stretchUVY);

    // Map into region UVs
    vec2 regionUV = tiledUV * u_uvScale + u_uvOffset;

    vec4 color = texture2D(u_texture, regionUV);
    color.a *= u_opacity;

    if (color.a < 0.01) discard;

    gl_FragColor = color;
}



