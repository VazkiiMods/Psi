#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D DepthSampler;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

const float DEPTH_EDGE_THRESHOLD = 0.0015;

vec3 normalBits(vec3 c) {
    return mod(floor(c * 255.0 + 0.5), 2.0);
}

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
    float silhouetteEdge = clamp(length(vec2(gx, gy)), 0.0, 1.0);

    float depthEdge = 0.0;
    if(c11.a > 0.5) {
        float d11 = texture(DepthSampler, texCoord).r;
        float dl = texture(DepthSampler, texCoord + oneTexel * vec2(-1.0, 0.0)).r;
        float dr = texture(DepthSampler, texCoord + oneTexel * vec2( 1.0, 0.0)).r;
        float du = texture(DepthSampler, texCoord + oneTexel * vec2( 0.0, -1.0)).r;
        float dd = texture(DepthSampler, texCoord + oneTexel * vec2( 0.0,  1.0)).r;
        if(c01.a > 0.5) depthEdge = max(depthEdge, abs(d11 - dl));
        if(c21.a > 0.5) depthEdge = max(depthEdge, abs(d11 - dr));
        if(c10.a > 0.5) depthEdge = max(depthEdge, abs(d11 - du));
        if(c12.a > 0.5) depthEdge = max(depthEdge, abs(d11 - dd));
        depthEdge = step(DEPTH_EDGE_THRESHOLD, depthEdge);
    }

    float normalEdge = 0.0;
    if(c11.a > 0.5) {
        vec3 bits11 = normalBits(c11.rgb);
        if(c01.a > 0.5 && any(notEqual(bits11, normalBits(c01.rgb)))) normalEdge = 1.0;
        if(c21.a > 0.5 && any(notEqual(bits11, normalBits(c21.rgb)))) normalEdge = 1.0;
        if(c10.a > 0.5 && any(notEqual(bits11, normalBits(c10.rgb)))) normalEdge = 1.0;
        if(c12.a > 0.5 && any(notEqual(bits11, normalBits(c12.rgb)))) normalEdge = 1.0;
    }

    float total = max(max(silhouetteEdge, depthEdge), normalEdge);

    vec3 outColor = c00.rgb * c00.a + c10.rgb * c10.a + c20.rgb * c20.a
            + c01.rgb * c01.a + c11.rgb * c11.a + c21.rgb * c21.a
            + c02.rgb * c02.a + c12.rgb * c12.a + c22.rgb * c22.a;

    fragColor = vec4(outColor / 9.0, total);
}
