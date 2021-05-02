import kotlin.math.cos
import kotlin.math.sin

object TransformMatrixFabric {
    fun rotateX(angle: Number): Matrix {
        val matrix = Matrix(4, 4)
        matrix[4, 4] = 1.0
        matrix[1, 1] = 1.0
        matrix[2, 2] = cos(angle.toDouble())
        matrix[2, 3] = sin(angle.toDouble())
        matrix[3, 2] = -sin(angle.toDouble())
        matrix[3, 3] = cos(angle.toDouble())
        return matrix
    }

    fun rotateY(angle: Number): Matrix {
        val matrix = Matrix(4, 4)
        matrix[4, 4] = 1.0
        matrix[2, 2] = 1.0
        matrix[1, 1] = cos(angle.toDouble())
        matrix[1, 3] = -sin(angle.toDouble())
        matrix[3, 1] = sin(angle.toDouble())
        matrix[3, 3] = cos(angle.toDouble())
        return matrix
    }

    fun rotateZ(angle: Number): Matrix {
        val matrix = Matrix(4, 4)
        matrix[4, 4] = 1.0
        matrix[3, 3] = 1.0
        matrix[1, 1] = cos(angle.toDouble())
        matrix[1, 2] = sin(angle.toDouble())
        matrix[2, 1] = -sin(angle.toDouble())
        matrix[2, 2] = cos(angle.toDouble())
        return matrix
    }

    fun translate(x: Number, y: Number, z: Number): Matrix {
        val matrix = Matrix(4, 4)
        matrix[1, 1] = 1.0
        matrix[2, 2] = 1.0
        matrix[3, 3] = 1.0
        matrix[4, 4] = 1.0
        matrix[4, 1] = x
        matrix[4, 2] = y
        matrix[4, 3] = z
        return matrix
    }

    fun perspectiveZ(z: Number): Matrix {
        val matrix = Matrix(4, 4)
        matrix[1, 1] = 1.0
        matrix[2, 2] = 1.0
        matrix[3, 3] = 1.0
        matrix[4, 4] = 1.0
        matrix[3, 4] = z
        return matrix
    }

    fun perspectiveY(y: Number): Matrix {
        val matrix = Matrix(4, 4)
        matrix[1, 1] = 1.0
        matrix[2, 2] = 1.0
        matrix[3, 3] = 1.0
        matrix[4, 4] = 1.0
        matrix[2, 4] = y
        return matrix
    }

    fun perspectiveX(x: Number): Matrix {
        val matrix = Matrix(4, 4)
        matrix[1, 1] = 1.0
        matrix[2, 2] = 1.0
        matrix[3, 3] = 1.0
        matrix[4, 4] = 1.0
        matrix[1, 4] = x
        return matrix
    }

    fun scale(x: Number, y: Number, z: Number): Matrix {
        val matrix = Matrix(4,4)
        matrix[1,1] = x
        matrix[2,2] = y
        matrix[3,3] = z
        matrix[4,4] = 1.0
        return matrix
    }
}