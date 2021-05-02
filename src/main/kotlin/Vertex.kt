import kotlin.math.sqrt

data class Vertex(var x: Double, var y: Double, var z: Double) {

    operator fun plus(b: Vertex) = Vertex(x + b.x, y + b.y, z + b.z)

    operator fun minus(b: Vertex) = Vertex(x - b.x, y - b.y, z - b.z)

    operator fun times(b: Number) = Vertex(x * b.toDouble(), y * b.toDouble(), z * b.toDouble())

    operator fun div(b: Number) = Vertex(x / b.toDouble(), y / b.toDouble(), z / b.toDouble())

    operator fun unaryMinus() = Vertex(-x, -y, -z)

    fun length() = sqrt(x*x + y*y + z*z)

    override fun toString(): String = "($x, $y, $z)"

    override fun hashCode(): Int {
        return super.hashCode()
    }
}