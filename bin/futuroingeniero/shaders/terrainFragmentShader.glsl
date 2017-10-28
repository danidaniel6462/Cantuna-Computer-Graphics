#version 400 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal; 
in vec3 toLightVector[4];
in vec3 toCameraVector;
in float visibilidad;

out vec4 salida_Color;

/* texturas:
	
	- fondo general del plano
	- variable para obtener el color rojo del blendMap
	- variable para obtener el color green del blendMap
	- variable para obtener el color blue del blendMap
	- la textura mezclada (BlendMap) 
	
*/
uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;

uniform vec3 luzColor[4];
uniform vec3 atenuacion[4];
uniform float shineDamper;
uniform float reflectivity; 
// vec3 uniforme ya que a futuro será necesario ya que cambiaremos el día a la noche
uniform vec3 cieloColor;

void main(void){

	// variable que nos dirá que cantidad de cada textura tomará para realizar la mezcla
	// esta línea crería un mosaico de la textura
	vec4 blendMapColor = texture(blendMap, pass_textureCoords);
	// calculamos cuánto del fondo negro queremos tener en nuestra textura
	float backTextureAmount = 1 - (blendMapColor.r + blendMapColor.g + blendMapColor.b);
	vec2 tiledCoords = pass_textureCoords * 40.0;
	// calculamos el color de todas las baldozas (tiled)
	vec4 backgroundTextureColor = texture(backgroundTexture, tiledCoords) * backTextureAmount;
	vec4 rTextureColor = texture(rTexture, tiledCoords) * blendMapColor.r;
	vec4 gTextureColor = texture(gTexture, tiledCoords) * blendMapColor.g;
	vec4 bTextureColor = texture(bTexture, tiledCoords) * blendMapColor.b;
	
	// finalmente se calcula el color total de la mezcla
	vec4 totalColor = backgroundTextureColor + rTextureColor + gTextureColor + bTextureColor;
	
	// normalizamos los vectores surfaceNormal y toLightVector 
	vec3 unitarioNormal = normalize(surfaceNormal);
	vec3 unitarioVectorToCamera = normalize(toCameraVector);
	
	// inicializa variables para tomar el total de luz difusa y especular
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);
	
	// Se realiza 4 veces el cálculo para las luces en el escenario 
	for(int i = 0; i < 4; i++){
		// distancia de la luz al objeto
		float distancia = length(toLightVector[i]);
		float atenuacionFactor = atenuacion[i].x + (atenuacion[i].y * distancia) + (atenuacion[i].z * pow(distancia, 2));
		vec3 unitarioLuzVector = normalize(toLightVector[i]);
		// realizamos el cálculos producto escalar o DOT entre los dos vectores normalizados
		// este cálculo se realiza para que cada píxel represente la intensidad de la luz
		float nDotLuz = dot(unitarioNormal, unitarioLuzVector);
		// la operación dot devuelve valores incluso negativos, lo que se hace es despreciar los valores negativos y ponerlos 0 si fuere el caso 
		float brillo = max(nDotLuz, 0.0);
		vec3 luzDireccion = - unitarioLuzVector;
		vec3 direccionLuzReflejada = reflect(luzDireccion, unitarioNormal);
		
		float specularFactor = dot(direccionLuzReflejada, unitarioVectorToCamera);
		specularFactor = max(specularFactor, 0.0);
		float damperFactor = pow(specularFactor, shineDamper);
		// multiplicamos el factor brillo por el color de la luz para obtener el color e intensidad final de cada pixel
		// lo que hace el GLSL es calcular la luz difusa y especular causada por cada na de las luces
		totalDiffuse = totalDiffuse  + (brillo * luzColor[i]) / atenuacionFactor;
		totalSpecular = totalSpecular + (damperFactor * reflectivity * luzColor[i]) / atenuacionFactor;
	}

	// tomamos valores entre la luz Difusa y 0.2 para asegurarnos que ninguna cara del modelo sea oscuro o negra, así se le da un color oscuro claro jeje, (yo me entiendo)
	totalDiffuse = max(totalDiffuse, 0.2);
	
	
	vec4 textureColor = totalColor;
		
	// el color depende de los atributos que se den a los objetos
	// el color final solo es una textura 
	
	// salida_Color = texture(muestreoTextura, pass_textureCoords);
	
	// color de salida, multiplicando la luz difusa con la textura del modelo
	// salida_Color = vec4(diffuse, 1.0) * texture(muestreoTextura, pass_textureCoords);
	
	// el color de salida es el final con un material que da brillo al objeto 
	salida_Color = vec4(totalDiffuse, 1.0) * textureColor + vec4(totalSpecular, 1.0);
	// mezclamos el color del cielo con el color final anterior y el cálculo de la visibilidad
	salida_Color = mix(vec4(cieloColor, 1.0), salida_Color, visibilidad);
	
	
}
