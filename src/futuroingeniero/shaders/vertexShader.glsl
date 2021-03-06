#version 400 core

/*
	GLSL
	
	Archivos creados para realizar c�lculos de sombredos, luces, colors, transparencias, etc
	Utilizando la GPU del computador
	
	La CPU env�a datos hacia la GPU para poder mostrar en pantalla gran calidad de im�genes
	pero el CPU se sobrecarga de c�lculos as� que es importante dividir la sobre carga de c�lculos entre la CPU y el GPU
	por este motivo se crean los ShadersPrograms.
	
	Dentro de los Shader tenemos dos tipos de archivos, se explica de manera resumida lo que son cada uno de estos archivos
	
	- VertexShader:  es el archivo que recibe los datos de la CPU para realizar los c�lculos dentro de la GPU
					 Este archivo realiza un s�lo c�lculo para cada v�rtice de los modelos
	- FragmentShader: este archivo recibe los datos de salida del VertexShader para realizar c�lculos en cada p�xel de los modelos
	
	Para realiza la transferencia de datos es importante decirle al programa que queremos calcular, para ello utilizamos las palabras:
	
	in (tipo dato) : la palabra in representan los datos de entrada para el Shader
	out (tipo dato): la palabra out representan los datos de salida que ser�n tomados por el el FragmentShader, para realizar
					 c�lculos en cada pixel.
	
	VARIABLES:
	
	vec{1|2|3}: vector {1|2|3} coordenadas
	sampler{1|2|3}D: variable encargada a acceder a una textura 
	 

	TIPO DE VARIABLES UNIFORM: son usados para realizar c�lculos uniformes dentro del escenario
					   ejemplo:
					   
				uniform vec3 colorLight; representa una variable que afectar� al escenario de manera uniforme
					   					en este caso si queremos representar el solo escribir�amos en RBG el color de sol.
				uniform vec3 positionLight; una vez que tenemos el color del sol se debe ubicar la posici�n del mismo
										por ejemplo en la vida real el sol var�a por cada hora en la ma�ana, as� entoces se puede
										representar que el sol sale del este y se oculta en el oeste o que simplemente est� a una hora determinada

*/


in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 pass_textureCoords;

// variable para vetor normal y vector que apunta hacia la luz
// el c�lculo de producto escalar entre estos dos vectores es 1 cuando tienen las misma direcci�n
// la luz ser�a lo m�s intensa en esa cara y si el valor es 0 la cara no recibe nada de luz 
out vec3 surfaceNormal;
out vec3 toLightVector[4];
// vector hacia la c�mara
out vec3 toCameraVector; 
// variable utilizada para la neblida en el escenario, es un factor llamado visibilidad
out float visibilidad;

// variable uniforme que recibe las transformaciones del modelo
uniform mat4 transformationMatrix;
// matriz de proyecci�n para ajustarlo a la c�mara 
uniform mat4 projectionMatrix;
// matriz de vista
uniform mat4 viewMatrix;
// posici�n de la luz, es un arreglo que tiene 4 luces, 1 por cada multiple luz
uniform vec3 luzPosicion[4];

// n�mero de filas que tiene la textura atlas
uniform float numeroFilas;
// variable para calcular el desplazamiento X & Y en la textura Atlas 
uniform vec2 offset;

uniform float usaFalsaIluminacion;

// densidad de la niebla 0.0035
const float density = 0.0015;
// gradiente de niebla
const float gradient = 5.0;

void main(void){

	vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
	vec4 positionRelativeToCam = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * positionRelativeToCam;
	// calculo para encontrar la textura que le corresponde al objeto
	pass_textureCoords = (textureCoords / numeroFilas) + offset;
	
	// guardamos la normal real del objeto
	vec3 realNormal = normal;
	
	if(usaFalsaIluminacion > 0.5){
		realNormal = vec3(0.0, 1.0, 0.0);
	}
	
	// calculamos las normales de cada superficie del modelo
	// para esto debemos tomar en cuanta que debemos multiplicar por la transformationMatrix, ya que nuestro modelo va a moverse dentro del espacio
	// finalmente al realizar la operaci�n debemos devolver valores xyz para poder realizar los c�lculos en el fragmentShader
	surfaceNormal = (transformationMatrix * vec4(realNormal, 0.0)).xyz;
	
	for (int i = 0; i < 4; i++){
		toLightVector[i] = luzPosicion[i] - worldPosition.xyz;
	}

	toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
	
	// distancia de un v�rtice a la c�mara para poder calcular la densidad de la niebla
	float distancia = length(positionRelativeToCam.xyz);
	// f�rmula para calcular la visibilidad de un objeto en el mundo
	visibilidad = exp(-pow((distancia * density), gradient));
	// el resultado de la visibilidad est� entre 0 y 1 utilizamos la funci�n clamp para calcular todos los valores entre 0 y 1
	visibilidad = clamp(visibilidad, 0.0, 1.0);
	
}
