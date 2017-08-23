#version 400

// cada vez que se toma un vecor3 de la dirección, entonces usamos la textura
// para enviarlas al samplerCube que está en 3D
in vec3 textureCoords;
out vec4 out_Color;

uniform samplerCube cubeMap;

void main(void){
    out_Color = texture(cubeMap, textureCoords);
}
