#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

void main(){
    vec4 c00 = texture(DiffuseSampler, texCoord + oneTexel * vec2(-1.0, -1.0));
    vec4 c10 = texture(DiffuseSampler, texCoord + oneTexel * vec2( 0.0, -1.0));
    vec4 c20 = texture(DiffuseSampler, texCoord + oneTexel * vec2( 1.0, -1.0));
    vec4 c01 = texture(DiffuseSampler, texCoord + oneTexel * vec2(-1.0,  0.0));
    vec4 c11 = texture(DiffuseSampler, texCoord);
    vec4 c21 = texture(DiffuseSampler, texCoord + oneTexel * vec2( 1.0,  0.0));
    vec4 c02 = texture(DiffuseSampler, texCoord + oneTexel * vec2(-1.0,  1.0));
    vec4 c12 = texture(DiffuseSampler, texCoord + oneTexel * vec2( 0.0,  1.0));
    vec4 c22 = texture(DiffuseSampler, texCoord + oneTexel * vec2( 1.0,  1.0));

    float gx = -c00.a - 2.0 * c01.a - c02.a + c20.a + 2.0 * c21.a + c22.a;
    float gy = -c00.a - 2.0 * c10.a - c20.a + c02.a + 2.0 * c12.a + c22.a;
    float total = clamp(length(vec2(gx, gy)), 0.0, 1.0);

    vec3 outColor = c00.rgb * c00.a + c10.rgb * c10.a + c20.rgb * c20.a
            + c01.rgb * c01.a + c11.rgb * c11.a + c21.rgb * c21.a
            + c02.rgb * c02.a + c12.rgb * c12.a + c22.rgb * c22.a;

    fragColor = vec4(outColor / 9.0, total);
}
