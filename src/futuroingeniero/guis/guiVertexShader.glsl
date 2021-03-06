#version 400

// entrada de vector posiciones del GUI que se renderizar�
in vec2 position;

// salida las coordenadas de la textura
out vec2 textureCoords;

// variable para generar una matriz de transformaci�n para poder mostrar el GUI en la pantalla
uniform mat4 transformationMatrix;

void main(void){

	// enviamos la posici�n del GUI en la pantalla con coordenadas solo x, y
	gl_Position = transformationMatrix * vec4(position, 0.0, 1.0);
	
	// vinculamos la textura sobre el modelo
	/*
	f�rmulas dibujar la textura del Gui en el centro de la pantalla
	
	texCoordX = posX + 1 / 2
	texCoordY = 1 - (posY + 1) / 2
	
	   (-1, 1)				  (1, 1)
	 * 		________________________
	 * 		|						|
	 * 		|						|
	 * 		|						|
	 * 		|						|
	 * 		|						|
	 * 		|						|
	 * 		|						|
	 * 		|						|
	 * 		|						|
	 *  	-------------------------
	 *   (-1, -1)				 (1, -1)
	 
	 
	 Despu�s del c�lculo se obtiene los datos 
	 	 (-1, 1)  por (0, 0)			 (1, 1) por (1, 0)
	 	 (-1, -1) por (0, 1)			 (1, -1) por (1, 1)
	 
	*/
	
	
	textureCoords = vec2((position.x + 1.0) / 2.0, 1 - (position.y + 1.0) / 2.0);
}
