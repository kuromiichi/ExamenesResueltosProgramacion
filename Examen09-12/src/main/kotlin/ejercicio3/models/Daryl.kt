package ejercicio3.models

data class Daryl(
    var vida: Int = 10,
    var municion: Int = 10,
    val posicion: IntArray = intArrayOf(0, 0),
    var judithEncontrada: Boolean = false
)