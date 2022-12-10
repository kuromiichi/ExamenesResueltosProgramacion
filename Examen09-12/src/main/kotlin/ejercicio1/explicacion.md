# Ruleta de la Muerte

## Clase Jugador

Se ha creado una clase `Jugador` para almacenar la información de cada 
jugador (nombre, posición de la bala, posición actual del tambor y si está 
vivo). Se han generado los _getters_ y _setters_ necesarios, así como una 
función 
para disparar y otra para avanzar la posición del tambor del revólver.

## Programa principal

La función `main()` llama a la función `juego()` que contiene el bucle 
principal del desarrollo del juego. Se crean los objetos `daryl` y `shane` 
que representan al jugador y al NPC respectivamente. Para facilitar pasarlos 
como parámetros se usa un par `jugadores`.

## Bucle del juego

La función `elegirResultadoDados()` llama a las funciones que obtienen los 
resultados predichos tanto por el jugador como por el NPC, y luego la 
función `tirarDados()` genera una tirada de dos D6. Se comprueba si hay un 
acierto en `comprobarResultadoDados()`, que en caso afirmativo llama a la 
función `disparar()`, que hace que el jugador que acierta intente disparar 
al otro. Si la bala está en la posición actual del tambor, se produce el 
disparo y el jugador disparado muere, terminando el bucle.

## Resultado del juego

La función `resultadoJuego()` identifica al jugador vencedor y al perdedor e 
imprime por pantalla el resultado de la partida.