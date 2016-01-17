#version 120

uniform sampler2D bgl_RenderedTexture;

void main() {
    vec2 texcoord = vec2(gl_TexCoord[0]);
    vec4 color = texture2D(bgl_RenderedTexture, texcoord);

    float r = color.r * gl_Color.r;
    float g = color.g * gl_Color.g;
    float b = color.b * gl_Color.b;
    float a = color.a * gl_Color.a;

    gl_FragColor = vec4(r, g, b, a);
}
