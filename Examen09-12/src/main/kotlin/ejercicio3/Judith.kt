package ejercicio3

import ejercicio3.models.Daryl

val hospital = Array(7) { Array(8) { ' ' } }

/**
 * @author Emma Fernández Barranco (afbarranco@gmail.com)
 */
fun main() {
    inicializarHospital()
    simulacion()
}

/**
 * Inicializa el hospital por habitación
 */
fun inicializarHospital() {
    for (pasillo in hospital.indices) {
        for (habitacion in hospital[pasillo].indices) {
            hospital[pasillo][habitacion] = inicializarHabitacion()
        }
    }
    inicializarJudith()
}

/**
 * Inicializa la habitación
 * @return el contenido de la habitación
 */
fun inicializarHabitacion(): Char {
    val rng = (1..100).random()
    return when (rng) {
        in 1..30 -> 'Z'
        in 31..45 -> 'M'
        in 46..60 -> 'V'
        else -> '·'
    }
}

/**
 * Coloca a Judith en el hospital
 */
fun inicializarJudith() {
    var x: Int
    var y: Int
    do {
        x = (hospital.indices).random()
        y = (hospital[x].indices).random()
    } while (x == 0 && y == 0)
    hospital[x][y] = 'J'
}

/**
 * Imprime el hospital
 * @param daryl el objeto Daryl
 */
fun imprimirHospital(daryl: Daryl) {
    for (pasillo in hospital.indices) {
        for (habitacion in hospital[pasillo].indices) {
            if (daryl.posicion[0] == pasillo && daryl.posicion[1] == habitacion) {
                print("D")
            } else print(hospital[pasillo][habitacion])
        }
        println()
    }
}

/**
 * Contiene el bucle principal
 */
fun simulacion() {
    val daryl = Daryl()
    do {
        imprimirHospital(daryl)
        comprobarHabitacion(daryl)
        moverDaryl(daryl)
        Thread.sleep(500)
    } while (daryl.vida > 0 && !daryl.judithEncontrada)
    resultadoSimulacion(daryl)
}

/**
 * Comprueba el contenido de la habitación y ejecuta la función correspondiente
 * @param daryl el objeto Daryl
 */
fun comprobarHabitacion(daryl: Daryl) {
    val x = daryl.posicion[0]
    val y = daryl.posicion[1]
    when (hospital[x][y]) {
        'Z' -> dispararZombi(daryl)
        'M' -> {
            if (daryl.municion < 10) {
                daryl.municion++
                println("Daryl ha recogido munición (${daryl.municion})")
            } else println("La munición está llena")
        }

        'V' -> {
            if (daryl.vida < 10) {
                daryl.vida++
                println("Daryl ha recuperado vida (${daryl.vida})")
            } else println("Daryl está en perfecto estado")

        }

        'J' -> daryl.judithEncontrada = true
    }
    hospital[x][y] = '·'
}

/**
 * Dispara al zombi, puede fallar y recibir daño
 * @param daryl el objeto Daryl
 */
fun dispararZombi(daryl: Daryl) {
    if (daryl.municion > 0) {
        println("Daryl dispara al zombi")
        daryl.municion--
        val rng = (1..100).random()
        if (rng in (1..33)) {
            println("El disparo ha fallado, Daryl ataca con el machete pero recibe daño")
            daryl.vida--
        }
    } else {
        println("Daryl no tiene munición y ataca con su machete, pero recibe mucho daño")
        daryl.vida -= 2
    }
}

/**
 * Mueve a Daryl a una posición posible
 * @param daryl el objeto Daryl
 */
fun moverDaryl(daryl: Daryl) {
    var direccion: IntArray
    do {
        direccion = generarDireccion()
        if (daryl.posicion[0] + direccion[0] >= 0
            && daryl.posicion[0] + direccion[0] < hospital.size
            && daryl.posicion[1] + direccion[1] >= 0
            && daryl.posicion[1] + direccion[1] < hospital[0].size
        ) {
            daryl.posicion[0] += direccion[0]
            daryl.posicion[1] += direccion[1]
        }
    } while (daryl.posicion[0] + direccion[0] < 0
        || daryl.posicion[0] + direccion[0] >= hospital.size
        || daryl.posicion[1] + direccion[1] < 0
        || daryl.posicion[1] + direccion[1] >= hospital[0].size
    )
}


/**
 * Genera una dirección válida para mover a Daryl
 * @return el array con la dirección
 */
fun generarDireccion(): IntArray {
    val direccion = IntArray(2)
    direccion[0] = (-1..1).random()
    direccion[1] = if (direccion[0] == 0) arrayOf(-1, 1).random() else 0
    return direccion
}

/**
 * Imprime el resultado de la simulación
 * @param daryl el objeto Daryl
 */
fun resultadoSimulacion(daryl: Daryl) {
    if (daryl.vida <= 0) {
        println("Daryl ha muerto antes de encontrar a Judith...")
    } else if (daryl.judithEncontrada) {
        println("Daryl ha encontrado a Judith y ambos consiguen escapar del hospital")
    }
}