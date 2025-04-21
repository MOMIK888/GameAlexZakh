#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_texture;
varying vec2 v_texCoord0;

float toonify(float intensity) {
    if (intensity > 0.9)
        return 1.0;
    else if (intensity > 0.7)
        return 0.8;
    else if (intensity > 0.4)
        return 0.6;
    else if (intensity > 0.2)
        return 0.4;
    else
        return 0.2;
}

vec3 saturateColor(vec3 color, float intensity) {
    float gray = dot(color, vec3(0.299, 0.587, 0.114));
    return mix(vec3(gray), color, intensity);
}

void main() {
    vec4 color = texture2D(u_texture, v_texCoord0);

    // Convert brightness to discrete steps
    float brightness = max(color.r, max(color.g, color.b));
    float factor = toonify(brightness);

    // Boost color saturation for cartoony pop
    vec3 saturated = saturateColor(color.rgb, 1.5);

    gl_FragColor = vec4(factor * saturated, color.a);
}

