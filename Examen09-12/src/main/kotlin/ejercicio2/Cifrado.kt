package ejercicio2

// Constante que almacena el sistema de rotación del cifrado
const val SISTEMA_ROTACION = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"

/**
 * Función principal del programa
 * @author Emma Fernández Barranco (afbarranco@gmail.com)
 */
fun main() {
    val opcion = elegirCifrarDescifrar()
    val cadena = pedirCadena()
    val clave = pedirClaveCifrado()
    val cadenaProcesada = when (opcion) {
        1 -> cifrar(cadena, clave)
        2 -> descifrar(cadena, clave)
        else -> ""
    }
    println("La cadena procesada es: $cadenaProcesada")
}

/**
 * Permite elegir si cifrar o descifrar una cadena
 * @return el entero correspondiente a la opción elegida
 */
fun elegirCifrarDescifrar(): Int {
    var opcion: Int
    do {
        print("Elige 1 para cifrar o 2 para descifrar: ")
        opcion = readln().toIntOrNull() ?: 0
        if (opcion !in (1..2)) println("Elige una opción válida")
    } while (opcion !in (1..2))
    return opcion
}

/**
 * Permite al usuario introducir la cadena a procesar
 * @return la cadena elegida
 */
fun pedirCadena(): String {
    print("Introduce una cadena para procesar:\n> ")
    return readln()
}

/**
 * Obtiene la clave de cifrado elegida por el usuario
 * @return el par de enteros que componen la clave
 */
fun pedirClaveCifrado(): Pair<Int, Int> {
    var x: Int
    var y: Int
    do {
        print("Introduce el primer número de la clave de cifrado: ")
        x = readln().toIntOrNull() ?: 0
        if (x <= 0) println("Elige un número válido (entero mayor que 0)")
    } while (x <= 0)
    do {
        print("Introduce el segundo número de la clave de cifrado: ")
        y = readln().toIntOrNull() ?: 0
        if (y <= 0) println("Elige un número válido (entero mayor que 0)")
    } while (y <= 0)
    return Pair(x, y)
}

/**
 * Cifra la cadena elegida
 * @param cadena cadena a cifrar
 * @param clave par de enteros que componen la clave
 * @return cadena cifrada
 */
fun cifrar(cadena: String, clave: Pair<Int, Int>): String {
    val primeraMitad = cadena.substring(0, cadena.length / 2)
    val segundaMitad = cadena.substring(cadena.length / 2)
    val cadenaCifrada = StringBuilder()
    cadenaCifrada.append(cifrarMitad(segundaMitad, clave.second * -1))
    cadenaCifrada.append("/")
    cadenaCifrada.append(cifrarMitad(primeraMitad, clave.first))
    return cadenaCifrada.toString()
}

/**
 * Cifra o descifra una determinada parte de la cadena
 * @param cadenaOriginal parte de la cadena a procesar
 * @clave componente relevante de la clave
 * @return cadena cifrada
 */
fun cifrarMitad(cadenaOriginal: String, clave: Int): String {
    val cadenaCifrada = StringBuilder()
    val caracteresCifrables = """[a-zA-Z]""".toRegex()
    for (caracter in cadenaOriginal) {
        if (caracter.toString().matches(caracteresCifrables)) {
            cadenaCifrada.append(cifrarCaracter(caracter, clave))
        } else cadenaCifrada.append(caracter)
    }
    return cadenaCifrada.toString()
}

/**
 * Cifra o descifra un caracter en función de la clave y el sistema de rotaación
 * @param caracter caracter a procesar
 * @param clave componente relevante de la clave
 * @return caracter cifrado
 */
fun cifrarCaracter(caracter: Char, clave: Int): Char {
    val posicionCaracter = SISTEMA_ROTACION.indexOf(caracter)
    return SISTEMA_ROTACION[(posicionCaracter + clave + SISTEMA_ROTACION.length) % SISTEMA_ROTACION.length]
}

/**
 * Descifra la cadena elegida
 * @param cadena cadena a descifrar
 * @param clave par de enteros que componen la clave
 * @return cadena descifrada
 */
fun descifrar(cadena: String, clave: Pair<Int, Int>): String {
    val cadenaDescifrada = StringBuilder()
    val mitades = cadena.split("/")
    cadenaDescifrada.append(cifrarMitad(mitades[1], clave.first * -1))
    cadenaDescifrada.append(cifrarMitad(mitades[0], clave.second))
    return cadenaDescifrada.toString()
}