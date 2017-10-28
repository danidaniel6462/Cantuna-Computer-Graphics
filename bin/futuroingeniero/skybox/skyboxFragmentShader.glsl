#version 400

// cada vez que se toma un vecor3 de la dirección, entonces usamos la textura
// para enviarlas al samplerCube que está en 3D
in vec3 textureCoords;
out vec4 out_Color;

// se pueden crear tantos CubeMap (texturas SkyBox) como se quiera, ya que se pueden presentar diferentes momentos del día o mostrar otros lugares
uniform samplerCube cubeMap;
uniform samplerCube cubeMap2;
// este factor se utilizará con valores 0 o 1, donde
// o representa la 1 textura y 0 la otra textura, el valor intermedio es la mezcla de las 2 texturas
uniform float blendFactor;
uniform vec3 fogColor;

/*
   límites para establecer la niebla sobre el SkyBox

  se puede escribir 2 límites en la textura del SkyBox que son:
   - Límite inferior, donde la textura es de color de la neblina
   - Límite superior, donde la textura es el color real del SkyBox
   Dentro de los límites el color de la textura va cambiando linealmente, con un efecto de degradado
   Así:
   Límite inferior = 0; es el centro del Skybox
   Límite superior = 30; un valor ligeramente superior al horizonte :3

 */

const float inferiorLimite = 0.0;
const float superiorLimite = 30.0;

void main(void){
	vec4 texture1 = texture(cubeMap, textureCoords);
	vec4 texture2 = texture(cubeMap2, textureCoords);
	// mezclamos las 2 texturas
    vec4 finalColor = mix(texture1, texture2, blendFactor);

    // representa la visibilidad del fragmento del Skybox
    // donde 0, significa que el fragmento estaría 	por debajo del límite inferior
    // y 1, significa que el fragmento estaría por encima del límite superior
    // lo que siginifica que se debe utilizar el color de la textura del SkyBox
    float factor = (textureCoords.y - inferiorLimite) / (superiorLimite - inferiorLimite);
    // despreciamos cualquier númoer que no esté en el rango de 0 y 1
    factor = clamp(factor, 0.0, 1.0);

    // realizamos la mezcla de los colores de la textura con el de la niebla
    // para llevar calcular el color de salida necesitamos utiliza rl factor
    out_Color = mix(vec4(fogColor, 1.0), finalColor, factor);
}
