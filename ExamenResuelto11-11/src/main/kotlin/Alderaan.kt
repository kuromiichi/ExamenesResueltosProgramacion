import com.github.ajalt.mordant.rendering.BorderStyle
import com.github.ajalt.mordant.table.table
import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.rendering.TextColors.*

// Valores globales porque tengo que ponerlos en todos lados y me estoy estresando con los parámetros
val dimensiones = pedirDimensiones()
val matrizCantidadMaxima = Array(dimensiones[0]) { IntArray(dimensiones[1]) }
val matrizCantidadRecogida = Array(dimensiones[0]) { IntArray(dimensiones[1]) }
val matrizTipos = Array(dimensiones[0]) { CharArray(dimensiones[1]) }
val matrizDescubierto = Array(dimensiones[0]) { BooleanArray(dimensiones[1]) }
const val TIEMPO_TOTAL = 4 * 5_000
const val CAPACIDAD_MAXIMA = 150

// Creación de objeto Terminal para decorar la salida
val t = Terminal()

fun main() {
    inicializarCuadriculas()
    simulacion()
}

/**
 * Imprime los resultados de la simulación.
 * @param tiempo el tiempo final de la simulación
 * @param capacidad la capacidad final de la simulación
 */
fun imprimirResultados(tiempo: Int, capacidad: Int) {
    val vectorCantidades = contarCantidades()
    print("Simulación finalizada: ")
    if (tiempo == TIEMPO_TOTAL) {
        println("tiempo total alcanzado")
    } else if (capacidad == CAPACIDAD_MAXIMA) {
        println("capacidad máxima alcanzada")
    }
    println("\nMateriales recogidos:")
    println("${gray("Roca")}: ${vectorCantidades[0]} kg")
    println("${yellow("Tierra")}: ${vectorCantidades[1]} kg")
    println("${red("Mineral")}: ${vectorCantidades[2]} kg")
    println("Otros: ${vectorCantidades[3]} kg\n")
    println("${red("Minerales")} encontrados:")
    mapaResultadosMejorado()
}

/**
 * Imprime el mapa de minerales al final de los resultados.
 */
fun mapaResultados() {
    for (i in -1..matrizTipos.size) {
        for (j in -1..matrizTipos[0].size) {
            when (i) {
                -1, matrizTipos.size -> {
                    if (j == -1 || j == matrizTipos[0].size) {
                        print("| ")
                    } else print("- ")
                }

                else -> {
                    when (j) {
                        -1, matrizTipos[0].size -> print("| ")
                        else -> {
                            when (matrizTipos[i][j]) {
                                'm' -> {
                                    if (matrizCantidadRecogida[i][j] > 0) {
                                        if (matrizCantidadRecogida[i][j] < 10) {
                                            print(" ${matrizCantidadRecogida[i][j]}")
                                        } else print(matrizCantidadRecogida[i][j])
                                    } else print(". ")
                                }

                                else -> print(". ")
                            }
                        }
                    }
                }
            }
        }
        println()
    }
    println()
}

fun mapaResultadosMejorado() {
    t.println(table {
        borderStyle = BorderStyle.ROUNDED
        body {
            for (i in matrizTipos.indices) {
                row {
                    for (j in matrizTipos[i].indices) {
                        if (matrizTipos[i][j] == 'm' && matrizCantidadRecogida[i][j] > 0) {
                            cell(red(matrizCantidadRecogida[i][j].toString()))
                        } else {
                            cell(" ")
                        }
                    }
                }
            }
        }
    })
}

/** Cuenta las cantidades de cada tipo de material recogido.
 * @return un vector con las cantidades
 */
fun contarCantidades(): IntArray {
    val vectorCantidades = IntArray(4)
    for (i in matrizCantidadRecogida.indices) {
        for (j in matrizCantidadRecogida[i].indices) {
            when (matrizTipos[i][j]) {
                'r' -> vectorCantidades[0] += matrizCantidadRecogida[i][j]
                't' -> vectorCantidades[1] += matrizCantidadRecogida[i][j]
                'm' -> vectorCantidades[2] += matrizCantidadRecogida[i][j]
                'o' -> vectorCantidades[3] += matrizCantidadRecogida[i][j]
            }
        }
    }
    return vectorCantidades
}

/**
 * Inicializa las matrices que necesitan valores iniciales.
 */
fun inicializarCuadriculas() {
    inicializarTipos()
    inicializarCantidades()
}

/**
 * Inicializa la matriz de cantidades máximas.
 */
fun inicializarCantidades() {
    for (i in matrizCantidadMaxima.indices) {
        for (j in matrizCantidadMaxima[i].indices) {
            if (matrizTipos[i][j] != 'n') {
                matrizCantidadMaxima[i][j] = (10..25).random()
            }
        }
    }
}

/**
 * Inicializa la matriz de tipos
 */
fun inicializarTipos() {
    for (i in matrizTipos.indices) {
        for (j in matrizTipos[i].indices) {
            val tipo = (1..5).random()
            matrizTipos[i][j] = when (tipo) {
                1 -> 'r'
                2 -> 't'
                3 -> 'm'
                4 -> 'o'
                else -> 'n'
            }
        }
    }
}

fun simulacion() {
    // R2-D2 empieza fuera del array para que al empezar el primer turno empiece en 0,0
    val posicionR2D2 = intArrayOf(0, -1)
    val direccionR2D2 = intArrayOf(0, 1)
    var capacidad = 0
    var tiempo = 0
    repeat(30) {
        println()
    }
    do {
        // Mover a R2-D2
        moverR2D2(posicionR2D2, direccionR2D2)
        // Recoger material
        if (tiempo % 4000 == 0) {
            capacidad += recogerMaterial(posicionR2D2, capacidad)
        }
        // Sortear dirección
        if (tiempo % 2000 == 0) {
            sortearDireccion(direccionR2D2)
        }
        // Imprimir cuadrícula
        imprimirCuadriculaMejorado(posicionR2D2)
        // Aumentar tiempo
        Thread.sleep(1000)
        tiempo += 1000
        repeat(30) {
            println()
        }
    } while (tiempo < TIEMPO_TOTAL && capacidad < CAPACIDAD_MAXIMA)
    imprimirResultados(tiempo, capacidad)
}

/**
 * Imprime la cuadrícula de tipos con R2-D2 encima (@).
 * @param posicionR2D2 la posición actual de R2-D2
 */
fun imprimirCuadricula(posicionR2D2: IntArray) {
    for (i in -1..matrizTipos.size) {
        for (j in -1..matrizTipos[0].size) {
            when (i) {
                -1, matrizTipos.size -> {
                    if (j == -1 || j == matrizTipos[0].size) {
                        print("| ")
                    } else print("- ")
                }

                else -> {
                    when (j) {
                        -1, matrizTipos[0].size -> print("| ")
                        else -> {
                            if (i == posicionR2D2[0] && j == posicionR2D2[1]) {
                                print("@ ")
                            } else {
                                print(
                                    when (matrizTipos[i][j]) {
                                        'n' -> ". "
                                        else -> "${matrizTipos[i][j]} "
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        println()
    }
    println()
}

/**
 * Versión mejorada de la función de imprimir cuadrícula. Usa Mordant.
 * @param posicionR2D2 la posición actual de R2-D2
 */
fun imprimirCuadriculaMejorado(posicionR2D2: IntArray) {
    t.println(table {
        borderStyle = BorderStyle.ROUNDED
        body {
            for (i in matrizTipos.indices) {
                row {
                    for (j in matrizTipos[i].indices) {
                        if (matrizDescubierto[i][j]) {
                            if (posicionR2D2[0] == i && posicionR2D2[1] == j) {
                                cell(blue("@"))
                            } else {
                                when (matrizTipos[i][j]) {
                                    'r' -> cell(gray("r"))
                                    't' -> cell(yellow("t"))
                                    'm' -> cell(red("m"))
                                    'o' -> cell("o")
                                    'n' -> cell(" ")
                                }
                            }
                        } else {
                            cell(magenta("?"))
                        }
                    }
                }
            }
        }
    })
}

/**
 * Sortea aleatoriamente la dirección de R2-D2 (norte, sur, este, oeste).
 * @param direccionR2D2 la dirección actual de R2-D2
 */
fun sortearDireccion(direccionR2D2: IntArray) {
    for (i in direccionR2D2.indices) {
        direccionR2D2[i] = 0
    }
    // Elegir dirección
    val direccion = (0..1).random()
    // Elegir sentido
    val sentido = intArrayOf(-1, 1).random()
    // Cambiar dirección y sentido
    direccionR2D2[direccion] = sentido
}

/**
 * Recoge el material en la posición de R2-D2 (si lo hay y hay capacidad suficiente).
 * @param posicionR2D2 la posición actual de R2-D2
 * @param capacidad la capacidad de almacenamiento de muestras de R2-D2
 * @return la cantidad de material recogida
 */
fun recogerMaterial(posicionR2D2: IntArray, capacidad: Int): Int {
    // Variables para fila y columna para acortar el código
    val f = posicionR2D2[0]
    val c = posicionR2D2[1]
    var cantidadRecogida = 0
    val cantidadRestante = matrizCantidadMaxima[f][c] - matrizCantidadRecogida[f][c]
    if (matrizTipos[f][c] != 'n' && matrizCantidadRecogida[f][c] < matrizCantidadMaxima[f][c]) {
        do {
            cantidadRecogida = (1..5).random()
            if (cantidadRecogida <= cantidadRestante && capacidad + cantidadRecogida <= CAPACIDAD_MAXIMA) {
                matrizCantidadRecogida[f][c] += cantidadRecogida
                print("Se ha recogido: $cantidadRecogida kg de ")
                when (matrizTipos[f][c]) {
                    'r' -> println(gray("roca"))
                    't' -> println(yellow("tierra"))
                    'm' -> println(red("mineral"))
                    'o' -> println("otros")
                }
            }
        } while (cantidadRecogida > cantidadRestante && capacidad + cantidadRecogida > CAPACIDAD_MAXIMA)
    } else println("No se ha recogido nada")
    return cantidadRecogida
}

/**
 * Cambia la posición de R2-D2 en la dirección correspondiente sin salirse de la cuadrícula.
 * @param posicionR2D2 la posición actual de R2-D2
 * @param direccionR2D2 la dirección a seguir
 */
fun moverR2D2(posicionR2D2: IntArray, direccionR2D2: IntArray) {
    for (i in posicionR2D2.indices) {
        if (posicionR2D2[i] + direccionR2D2[i] < 0 || posicionR2D2[i] + direccionR2D2[i] >= dimensiones[i]) {
            direccionR2D2[i] *= -1
        }
        posicionR2D2[i] += direccionR2D2[i]
    }
    matrizDescubierto[posicionR2D2[0]][posicionR2D2[1]] = true
}

/**
 * Pide las dimensiones de la cuadrícula al usuario
 * @return un vector con las dimensiones de la cuadrícula
 */
fun pedirDimensiones(): IntArray {
    val regexDimensiones = """[5-8]""".toRegex()
    val dimensiones = IntArray(2)
    for (i in dimensiones.indices) {
        do {
            println("Introduce el número de ${if (i == 0) "filas" else "columnas"}")
            val entrada = readln()
            if (entrada.matches(regexDimensiones)) {
                dimensiones[i] = entrada.toInt()
            } else {
                println("Error: la entrada debe ser un entero entre 5 y 8")
            }
        } while (dimensiones[i] == 0)
    }
    return dimensiones
}
