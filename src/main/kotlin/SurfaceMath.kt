import kotlin.math.pow

object SurfaceMath {

    fun binomCoeff(n: Int, i: Int): Double =
        factorial(n).toDouble() / (factorial(i).toDouble() * factorial(n - i))

    fun bernsteinPoly(n: Int, i: Int, u: Double) =
        binomCoeff(n, i) * (u.pow(i)) * (1 - u).pow(n - i)

    fun factorial(i: Int): Int {
        var a = 1
        repeat(i) {
            a *= (it + 1)
        }
        return a
    }
}