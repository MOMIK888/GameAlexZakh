#ifdef GL_ES
precision mediump float;
#endif

varying vec3 v_normal;
varying vec3 v_position;

uniform float u_lineThickness;
uniform float u_offset;

void main() {
    // Calculate the dot product of the normal and view direction
    vec3 viewDir = normalize(-v_position);
    float edge = dot(v_normal, viewDir);

    // Apply offset and thickness to create the black lines
    if (edge < u_lineThickness + u_offset && edge > u_lineThickness - u_offset) {
        gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0); // Black lines
    } else {
        gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0); // White fill
    }
}
