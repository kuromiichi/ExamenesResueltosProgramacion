package ejercicio1.models

class Jugador(private val nombre: String, private var posicionBala: Int = -1, private var vivo: Boolean = true) {
    private var posicionActual: Int = 0

    init {
        require(posicionBala in (1..6) || posicionBala == -1)
        posicionBala--
        if (posicionBala !in (0..5)) posicionBala = (0..5).random()
    }

    fun getNombre(): String {
        return nombre
    }

    fun getVivo(): Boolean {
        return vivo
    }

    fun morir() {
        this.vivo = false
    }

    fun disparar(): Boolean {
        if (posicionBala != posicionActual) {
            avanzarPosicion()
            return false
        }
        return true
    }

    private fun avanzarPosicion() {
        posicionActual = (posicionActual + 1) % 6
    }
}