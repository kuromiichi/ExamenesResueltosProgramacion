import java.lang.StringBuilder
import com.github.ajalt.mordant.rendering.TextColors.*

fun main() {
    // Index 0: R2-D2 Index 1: BB-8 Index 2: C-3PO
    val vectorPuntos = IntArray(3)
    juegoAdivinar(vectorPuntos)
    resultadoJuego(vectorPuntos)
}

/**
 * Imprime el resultado del juego con las puntuaciones ordenadas.
 * @param vectorPuntos el vector de puntos totales
 */
fun resultadoJuego(vectorPuntos: IntArray) {
    println("Resultado del juego:\n")
    val vectorNombres = arrayOf(blue("R2-D2"), red("BB-8"), yellow("C-3PO"))
    ordenarVectores(vectorPuntos, vectorNombres)
    for (i in vectorPuntos.indices) {
        println("Posición ${i + 1}: ${vectorNombres[i]} con ${vectorPuntos[i]} puntos")
    }
}

/**
 * Ordena los vectores de puntos y nombres para imprimir el resultado ordenado.
 * @param vectorPuntos el vector de puntos totales
 * @param vectorNombres el vector de nombres de jugadores
 */
fun ordenarVectores(vectorPuntos: IntArray, vectorNombres: Array<String>) {
    var aux: Int
    var aux2: String
    for (i in 0 until vectorPuntos.size - 1) {
        for (j in 0 until vectorPuntos.size - 1) {
            if (vectorPuntos[j] < vectorPuntos[j + 1]) {
                // Cambio de posición de puntos
                aux = vectorPuntos[j + 1]
                vectorPuntos[j + 1] = vectorPuntos[j]
                vectorPuntos[j] = aux
                // Cambio de posición de nombres
                aux2 = vectorNombres[j + 1]
                vectorNombres[j + 1] = vectorNombres[j]
                vectorNombres[j] = aux2
            }
        }
    }
}

/**
 * Contiene todas las instrucciones necesarias para el juego.
 * @param vectorPuntos el vector de puntos totales
 */
fun juegoAdivinar(vectorPuntos: IntArray) {
    do {
        val vectorCadenas = Array(3) { "" }
        val vectorAdivinanzas = IntArray(3)
        // Generar elecciones IA
        for (i in 0..1) {
            vectorCadenas[i] = generarCadena()
            vectorAdivinanzas[i] = generarAdivinanza(vectorAdivinanzas)
        }
        // Pedir elecciones del usuario
        vectorCadenas[2] = pedirCadena()
        vectorAdivinanzas[2] = pedirAdivinanza(vectorAdivinanzas)
        // Sumar ceros y elegir ganador
        val totalCeros = sumarCeros(vectorCadenas)
        print("La suma de los ceros era de $totalCeros, ")
        comprobarGanador(vectorPuntos, vectorAdivinanzas, totalCeros)
    } while (!comprobarVictoria(vectorPuntos))
}

/**
 * Comprueba el ganador de una ronda comparando el total de ceros con las adivinanzas de los jugadores.
 * @param vectorPuntos el vector de puntos totales
 * @param vectorAdivinanzas el vector de adivinanzas de los jugadores
 * @param totalCeros el total de ceros en las cadenas elegidas por los jugadores
 */
fun comprobarGanador(vectorPuntos: IntArray, vectorAdivinanzas: IntArray, totalCeros: Int) {
    var ganador = false
    for (i in vectorAdivinanzas.indices) {
        if (vectorAdivinanzas[i] == totalCeros) {
            vectorPuntos[i]++
            ganador = true
            when (i) {
                0 -> println("gana ${blue("R2-D2")}\n")
                1 -> println("gana ${red("BB-8")}\n")
                2 -> println("gana ${yellow("C-3PO")}\n")
            }
        }
    }
    if (!ganador) {
        println("nadie ha acertado")
    }
}

/**
 * Suma los ceros en las cadenas elegidas por los jugadores.
 * @param vectorCadenas el vector de las cadenas elegidas
 * @return el número total de ceros
 */
fun sumarCeros(vectorCadenas: Array<String>): Int {
    var totalCeros = 0
    for (cadena in vectorCadenas) {
        for (digito in cadena) {
            if (digito == '0') {
                totalCeros++
            }
        }
    }
    return totalCeros
}

/**
 * Pide la adivinanza al jugador y comprueba su validez.
 * @param vectorAdivinanzas el vector de adivinanzas de los jugadores
 * @return la adivinanza del jugador
 */
fun pedirAdivinanza(vectorAdivinanzas: IntArray): Int {
    val regexAdivinanza = """\d""".toRegex()
    var adivinanza: String
    do {
        var existe = false
        do {
            // Pedir entrada
            println("¿Cuántos ceros hay?")
            print("> ")
            adivinanza = readln()
            // Comprobar regex
            if (!adivinanza.matches(regexAdivinanza)) {
                println("Error: solo puede haber entre 0 y 9 ceros")
            }
        } while (!adivinanza.matches(regexAdivinanza))
        // Comprobar si ya existe ese número
        for (i in vectorAdivinanzas) {
            if (i == adivinanza.toInt()) {
                println("Error: ya se ha elegido ese número")
                existe = true
            }
        }
    } while (existe)
    return adivinanza.toInt()
}

/**
 * Pide una cadena binaria al jugador y comprueba su validez.
 * @return la cadena elegida
 */
fun pedirCadena(): String {
    val regexCadena = """[01]{3}""".toRegex()
    var cadena: String
    do {
        // Pedir entrada
        println("${yellow("C-3PO")} dime tu elección:")
        print("> ")
        cadena = readln()
        // Comprobar regex
        if (!cadena.matches(regexCadena)) {
            println("Error: sólo puedes escoger tres dígitos entre 0 y 1")
        }
    } while (!cadena.matches(regexCadena))
    return cadena
}

/**
 * Genera una adivinanza para la IA.
 * @param vectorAdivinanzas el vector de adivinanzas de los jugadores
 * @return la adivinanza del jugador
 */
fun generarAdivinanza(vectorAdivinanzas: IntArray): Int {
    var adivinanza = -1
    do {
        var existe = false
        val numero = (0..9).random()
        for (i in vectorAdivinanzas) {
            if (i == numero) {
                existe = true
            }
        }
        if (!existe) adivinanza = numero
    } while (existe)
    return adivinanza
}

/**
 * Genera una cadena para la IA.
 * @return la cadena del jugador
 */
fun generarCadena(): String {
    val cadena = StringBuilder()
    for (i in 1..3) {
        val numero = (0..1).random()
        cadena.append(numero.toString())
    }
    return cadena.toString()
}

/**
 * Comprueba si un jugador ha ganado (ha llegado a 3 puntos).
 * @param vectorPuntos el vector de puntos totales
 * @return si alguien ha ganado o no
 */
fun comprobarVictoria(vectorPuntos: IntArray): Boolean {
    for (jugador in vectorPuntos) {
        if (jugador >= 3) return true
    }
    return false
}
