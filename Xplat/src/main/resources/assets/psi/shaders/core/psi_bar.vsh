#version 150

in vec3 Position;
in vec4 Color;
in vec2 UV0;

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;
uniform float GameTime;

out vec2 texCoord0;
out vec4 textureColor;
out vec4 maskColor;
out vec4 vertexColor;
out float timeTicks;

void main() {
    vertexColor = Color;
    textureColor = texture(Sampler0, UV0);
    maskColor = texture(Sampler1, UV0);
    texCoord0 = UV0;
    timeTicks = GameTime * 24000;
}