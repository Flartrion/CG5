import kotlin.math.acos

object VectorMath {
    fun crossProduct(a: Vertex, b: Vertex): Vertex =
        Vertex((a.y * b.z) - (a.z - b.y), (a.x * b.z) - (a.z * b.x), (a.x * b.y) - (a.y * b.x))

    fun angleBetween(a: Vertex, b: Vertex): Double =
        acos(((a.x * b.x) + (a.y * b.y) + (a.z * b.z))/(a.length()*b.length()))
}