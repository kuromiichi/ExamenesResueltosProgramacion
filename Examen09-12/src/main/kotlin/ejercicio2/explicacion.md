# Cifrado

## Sistema de rotación

El sistema de rotación se ha definido como constante global para evitar
pasarlo como parámetro en todas las funciones, pudiendo acceder a ella desde
la función que lo necesita sin pasar por el resto.

## Funcionamiento

La función `main()` permite elegir si cifrar o descifrar una cadena con
`elegirCifrarDescifrar()`. Luego se pide la cadena con `pedirCadena()`, y se
repite con la clave en `pedirClaveCifrado()`. En función de la opción
elegida, se llama a `cifrar()` o `descifrar()` sobre la cadena en función de
la clave.

## Cifrado/descifrado

Para cifrar la cadena, el programa la divide en mitades, y luego crea un
`StringBuilder` que almacena la cadena procesada. El programa va cifrando
carácter por carácter, comprobando si es cifrable y procesándolo en caso
afirmativo. Para procesarlo, la función `cifrarCaracter()` obtiene la
posición del caracter en la lista del sistema de rotación y le suma el valor
de la clave, haciendo después el módulo del resultado con la longitud del
sistema de rotación para que una vez el índice salga de la cadena vuelva a
entrar por el principio.

Finalmente, se añaden las mitades en orden inverso con una barra ("/") en 
medio al `StringBuilder`, y se devuelve en forma de cadena.

El descifrado separa las mitades desde la barra con la función `split("/")` 
y aplica el cifrado inverso usando la misma función que el cifrado, pero 
modificando la clave para que el descifrado sea correcto.