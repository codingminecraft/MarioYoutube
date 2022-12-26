#type vertex
#version 330 core
layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aTexCoords;
layout (location=3) in float aTexId;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;
out vec2 fTexCoords;
out float fTexId;

void main()
{
    fColor = aColor;
    fTexCoords = aTexCoords;
    fTexId = aTexId;

    gl_Position = uProjection * uView * vec4(aPos, 1.0);
}

#type fragment
#version 330 core

in vec4 fColor;
in vec2 fTexCoords;
in float fTexId;

uniform sampler2D uTextures[8];

out vec4 color;

void main()
{
    if (fTexId > 0) {
        // NOTE: If you're on AMD GPUs try commenting this line out and replacing it with
        // the next few lines. The issue here is dynamic indexing is undefined behavior
        // in GLSL, so this is not guaranteed to work on all hardware
        //
        // int id = int(fTexId);
        // color = fColor * texture(uTextures[id], fTexCoords);

        int id = int(fTexId);
        switch (id) {
            case 0:
                color = fColor * texture(uTextures[0], fTexCoords);
            break;
            case 1:
                color = fColor * texture(uTextures[1], fTexCoords);
            break;
            case 2:
                color = fColor * texture(uTextures[2], fTexCoords);
            break;
            case 3:
                color = fColor * texture(uTextures[3], fTexCoords);
            break;
            case 4:
                color = fColor * texture(uTextures[4], fTexCoords);
            break;
            case 5:
                color = fColor * texture(uTextures[5], fTexCoords);
            break;
            case 6:
                color = fColor * texture(uTextures[6], fTexCoords);
            break;
            case 7:
                color = fColor * texture(uTextures[7], fTexCoords);
            break;
        }
    } else {
        color = fColor;
    }
}