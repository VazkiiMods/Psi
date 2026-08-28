#version 150

uniform float PsiBarPercentile;
uniform int PsiBarOverflowed;

in vec2 texCoord0;
in vec4 textureColor;
in vec4 maskColor;
in vec4 vertexColor;
in float timeTicks;

out vec4 fragColor;

void main() {
    float maskgs = (maskColor.r + maskColor.g + maskColor.b) / 3.0;

    float r = textureColor.r * vertexColor.r;
    float g = textureColor.g * vertexColor.g;
    float b = textureColor.b * vertexColor.b;
    float a = textureColor.a;

    float exr1 = sin(texCoord0.x * 2 + texCoord0.y * 10 + timeTicks * 0.035);
    float exr2 = sin(texCoord0.x * 20 + texCoord0.y * 2 + timeTicks * 0.15);
    float exr3 = sin(texCoord0.x * 1 + texCoord0.y * 90 + timeTicks * 0.75);

    float w1 = (cos(timeTicks * 0.1) + 1) * 0.5;
    float w2 = (sin(timeTicks * 0.08) + 1) * 0.5;
    float w3 = (cos(timeTicks * 0.001) + 1) * 0.5;

    float w = w1 + w2 + w3;
    float exr = (exr1 * w1 + exr2 * w2 + exr3 * w3) / w * 0.1;

    r = PsiBarOverflowed == 1 ? 1.0 : r + exr;
    g -= exr;
    b = PsiBarOverflowed == 1 ? b - 0.2 : b;

    r = max(0, min(1, r));
    g = max(0, min(1, g));
    b = max(0, min(1, b));
    a = maskgs <= PsiBarPercentile ? a : 0;

    fragColor = vec4(r, g, b, a);
}
