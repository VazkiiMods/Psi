#version 120

uniform int time; // Passed in, see ShaderHelper.java

uniform float percentile; // Passed in via Callback
uniform sampler2D image;
uniform sampler2D mask;

void main() {
    vec2 texcoord = vec2(gl_TexCoord[0]);
    vec4 color = texture2D(image, texcoord);
    vec4 maskColor = texture2D(mask, texcoord);
    float maskgs = (maskColor.r + maskColor.g + maskColor.b) / 3.0;

    float r = color.r * gl_Color.r;
    float g = color.g * gl_Color.g;
    float b = color.b * gl_Color.b;
    float a = color.a; // Ignore gl_Color.a as we don't want to make use of that for the dissolve effect

    float exr1 = sin(texcoord.x * 2 + texcoord.y * 10 + time * 0.035);
    float exr2 = sin(texcoord.x * 20 + texcoord.y * 2 + time * 0.15);
    float exr3 = sin(texcoord.x * 1 + texcoord.y * 90 + time * 0.75);

    float w1 = (cos(time * 0.1) + 1) * 0.5;
    float w2 = (sin(time * 0.08) + 1) * 0.5;
    float w3 = (cos(time * 0.001) + 1) * 0.5;

    float w = w1 + w2 + w3;
    float exr = (exr1 * w1 + exr2 * w2 + exr3 * w3) / w * 0.1;

    r += exr;
    g -= exr;

    r = max(0, min(1, r));
    g = max(0, min(1, g));
    b = max(0, min(1, b));

    if(maskgs <= percentile)
    	gl_FragColor = vec4(r, g, b, a);
    else gl_FragColor = vec4(r, g, b, 0);
}
