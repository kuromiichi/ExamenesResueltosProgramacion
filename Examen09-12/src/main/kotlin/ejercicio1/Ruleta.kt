package ejercicio1

import ejercicio1.models.Jugador

/**
 * @author Emma Fernández Barranco (afbarranco@gmail.com)
 */
fun main() {
    println("Juego de la Ruleta de la Muerte")
    juego()
}

/**
 * Función principl del juego de la Ruleta de la Muerte, contiene el bucle principal
 */
fun juego() {
    // Creación de jugadores
    val daryl = Jugador("Daryl", 1)
    val shane = Jugador("Shane")
    val jugadores = Pair(daryl, shane)

    println("Los jugadores han elegido la posición de su bala")
    do {
        val resultados = elegirResultadoDados()
        val tirada = tirarDados()
        comprobarResultadoDados(resultados, tirada, jugadores)
    } while (daryl.getVivo() && shane.getVivo())
    resultadoJuego(jugadores)
}

/**
 * Contiene las funciones para elegir resultados de la tirada de dados
 * @return los resultados elegidos por los jugadores
 */
fun elegirResultadoDados(): Pair<Int, Int> {
    val resultadoDaryl = elegirResultadoDaryl()
    val resultadoShane = elegirResultadoShane(resultadoDaryl)
    return Pair(resultadoDaryl, resultadoShane)
}

/**
 * Permite al jugador elegir el resultado de la tirada de dados
 * @return el resultado elegido por el jugador
 */
fun elegirResultadoDaryl(): Int {
    var resultado: Int
    do {
        print("Daryl, elige el resultado de la tirada de dados (entre 2 y 12) ")
        resultado = readln().toIntOrNull() ?: 0
        if (resultado !in (2..12)) {
            println("Introduce un resultado válido (entre 2 y 12)")
        }
    } while (resultado !in (2..12))
    return resultado
}

/**
 * Genera el resultado elegido por el NPC aleatoriamente
 * @param resultadoDaryl el resultado elegido por el jugador
 * @return el resultado elegido por el NPC
 */
fun elegirResultadoShane(resultadoDaryl: Int): Int {
    var resultado: Int
    do {
        resultado = (2..12).random()
    } while (resultado == resultadoDaryl)
    return resultado
}

/**
 * Genera una tirada aleatoria de dos dados D6
 * @return la tirada generada
 */
fun tirarDados(): Int {
    return (2..12).random()
}

/**
 * Comprueba si un jugador ha acertado el resultado de la tirada de dados y le permite disparar al otro jugador
 * @param resultados los resultados elegidos por los jugadores
 * @param tirada la tirada de dados generada
 * @param jugadores par que contiene los objetos Jugador
 */
fun comprobarResultadoDados(resultados: Pair<Int, Int>, tirada: Int, jugadores: Pair<Jugador, Jugador>) {
    if (resultados.first == tirada) {
        disparar(jugadores.first, jugadores.second)
    } else if (resultados.second == tirada) {
        disparar(jugadores.second, jugadores.first)
    } else {
        println("Ninguno ha acertado")
    }
}

/**
 * El jugador que acierta la tirada dispara al otro jugador, con posibilidad de matarlo
 * @param disparador el jugador que dispara
 * @param disparado el jugador que recibe el disparo
 */
fun disparar(disparador: Jugador, disparado: Jugador) {
    println("${disparador.getNombre()} dispara")
    val balaDisparada = disparador.disparar()
    if (balaDisparada) {
        println("La bala de ${disparador.getNombre()} ha sido disparada")
        disparado.morir()
    }
}

/**
 * Imprime por pantalla el resultado del juego
 * @param jugadores par que contiene los objetos Jugador
 */
fun resultadoJuego(jugadores: Pair<Jugador, Jugador>) {
    val vencedor = if (jugadores.first.getVivo()) jugadores.first else jugadores.second
    val perdedor = if (vencedor == jugadores.first) jugadores.second else jugadores.first
    println("${perdedor.getNombre()} ha muerto, ${vencedor.getNombre()} es el vencedor")
}