#version 400

in vec3 position;
// vector 3 de la dirección hacia la que apunta el vector al CubeMap
out vec3 textureCoords;

// matriz que muestra la proyección en la Cámara
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(void){
	
	gl_Position = projectionMatrix * viewMatrix * vec4(position, 1.0); 
	textureCoords = position;
	
}
