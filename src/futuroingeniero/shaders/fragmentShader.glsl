#version 400 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal; 
in vec3 toLightVector[4];
in vec3 toCameraVector;
in float visibilidad;

out vec4 salida_Color;

uniform sampler2D muestreoTextura;
// color de cada una de las cuatro luces
uniform vec3 luzColor[4];
// se utiliza 4 valores de atenuaci�n, uno por cada luz creada
uniform vec3 atenuacion[4];
uniform float shineDamper;
uniform float reflectivity; 
// vec3 uniforme ya que a futuro ser� necesario ya que cambiaremos el d�a a la noche
uniform vec3 cieloColor;

void main(void){

	// normalizamos los vectores surfaceNormal y toLightVector 
	vec3 unitarioNormal = normalize(surfaceNormal);
	vec3 unitarioVectorToCamera = normalize(toCameraVector);
	
	// inicializa variables para tomar el total de luz difusa y especular
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);
	
	// Se realiza 4 veces el c�lculo para las luces en el escenario 
	for(int i = 0; i < 4; i++){
		// distancia de la luz al objeto
		float distancia = length(toLightVector[i]);
		float atenuacionFactor = atenuacion[i].x + (atenuacion[i].y * distancia) + (atenuacion[i].z * distancia * distancia);
		vec3 unitarioLuzVector = normalize(toLightVector[i]);	
		// realizamos el c�lculos producto escalar o DOT entre los dos vectores normalizados
		// este c�lculo se realiza para que cada p�xel represente la intensidad de la luz
		float nDotLuz = dot(unitarioNormal, unitarioLuzVector);
		// la operaci�n dot devuelve valores incluso negativos, lo que se hace es despreciar los valores negativos y ponerlos 0 si fuere el caso 
		float brillo = max(nDotLuz, 0.0);
		vec3 luzDireccion = - unitarioLuzVector;
		vec3 direccionLuzReflejada = reflect(luzDireccion, unitarioNormal);
		float specularFactor = dot(direccionLuzReflejada, unitarioVectorToCamera);
		specularFactor = max(specularFactor, 0.0);
		float damperFactor = pow(specularFactor, shineDamper);
		// multiplicamos el factor brillo por el color de la luz para obtener el color e intensidad final de cada pixel
		// lo que hace el GLSL es calcular la luz difusa y especular causada por cada una de las luces
		totalDiffuse = totalDiffuse  + (brillo * luzColor[i]) / atenuacionFactor;
		totalSpecular = totalSpecular + (damperFactor * reflectivity * luzColor[i]) / atenuacionFactor;
	}
	
	// tomamos valores entre la luz Difusa y 0.2 para asegurarnos que ninguna cara del modelo sea oscuro o negra, as� se le da un color oscuro claro jeje, (yo me entiendo)
	totalDiffuse = max(totalDiffuse, 0.2);

	vec4 textureColor = texture(muestreoTextura, pass_textureCoords);
	// comprobamos el valor del alpha de las texturas para poder realizar la transparencia
	// si el valor es menor que 0.5 despresiamos ese dato y renderizamos la textura sin el canal alpha
	if(textureColor.a < 0.5) {
		discard;
	}

	// el color depende de los atributos que se den a los objetos
	
	// el color final solo es una textura 
	// salida_Color = textureColor;
	
	// color de salida, multiplicando la luz difusa con la textura del modelo
	// salida_Color = vec4(diffuse, 1.0) * textureColor;
	
	// el color de salida es el final con un material que da brillo al objeto 
	salida_Color = vec4(totalDiffuse, 1.0) * textureColor + vec4(totalSpecular, 1.0);
	// mezclamos el color del cielo con el color final anterior y el c�lculo de la visibilidad
	salida_Color = mix(vec4(cieloColor, 1.0), salida_Color, visibilidad);
	
	
}