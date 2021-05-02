import kotlin.IllegalArgumentException

data class Matrix(val rows: Int, val columns: Int) {
    private val matrix: Array<Double> = Array(rows * columns) { 0.0 }

    operator fun get(row: Int, column: Int): Double {
        if (row <= rows && column <= columns && row > 0 && column > 0)
            return matrix[(row - 1) * columns + column - 1]
        else
            throw IllegalArgumentException("get[$row, $column] in [$rows, $columns] matrix")
    }

    operator fun set(row: Int, column: Int, number: Number) {
        if (row <= rows && column <= columns && row > 0 && column > 0)
            matrix[(row - 1) * columns + column - 1] = number.toDouble()
        else
            throw IllegalArgumentException("set[$row, $column] in [$rows, $columns] matrix")
    }

    fun det(): Double {
        return if (rows == columns) {
            // Going with the triangle-shaped (aka Gaussian) determinant calculation method.
            // Thus, copying the whole matrix for the sake of safety
            val changedMatrix = this * 1.0
            for (rowBase in 1 until rows)
                for (rowChange in rowBase + 1 until rows + 1) {
                    val coefficient = -changedMatrix[rowChange, rowBase] / changedMatrix[rowBase, rowBase]
                    for (column in 1..columns)
                        changedMatrix[rowChange, column] += changedMatrix[rowBase, column] * coefficient
                }

            var a = 1.0
            for (i in 1..rows)
                a *= changedMatrix[i, i]
            a
        } else
            throw IllegalArgumentException("Tried to get of non square matrix")
    }

    operator fun plus(b: Matrix): Matrix {
        if (rows == b.rows && columns == b.columns) {
            val c = Matrix(rows, columns)
            for (i in matrix.indices)
                c.matrix[i] = matrix[i] + b.matrix[i]
            return c
        } else {
            throw IllegalArgumentException("Tried to sum matrices of different sizes")
        }
    }

    operator fun minus(b: Matrix): Matrix {
        if (rows == b.rows && columns == b.columns) {
            val c = Matrix(rows, columns)
            for (i in matrix.indices)
                c.matrix[i] = matrix[i] - b.matrix[i]
            return c
        } else {
            throw IllegalArgumentException("Tried to subtract matrices of different sizes")
        }
    }

    // DISREGARD
//    // Won't work matrices other than 3x3. Too lazy to fix.
//    operator fun times(b: Vector): Vector {
//        return Vector(get(0,0)*b.x + get(0,1)*b.y+get(0,2)*b.z,
//            get(1,0)*b.x + get(1,1)*b.y+get(1,2)*b.z,
//            get(2,0)*b.x + get(2,1)*b.y+get(2,2)*b.z)
//    }

    operator fun times(b: Matrix): Matrix {
        if (columns == b.rows) {
            val c = Matrix(rows, b.columns)
            for (i in 1..rows)
                for (j in 1..b.columns)
                    for (k in 1..columns) {
                        c[i, j] = c[i, j] + this[i, k] * b[k, j]
                    }
            return c
        } else
            throw IllegalArgumentException("Matrix multiplication error")
    }

    operator fun times(b: Number): Matrix {
        val c = Matrix(rows, columns)
        for (i in matrix.indices)
            c.matrix[i] = matrix[i] * b.toDouble()
        return c
    }

    override fun toString(): String {
        var s = String()
        for (i in 1..rows) {
            for (j in 1..columns) {
                s += get(i, j)
                s += ' '
            }
            s += '\n'
        }
        return s
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (other !is Matrix)
            return false
        else {
            if (rows != other.rows || columns != other.columns)
                return false
            for (i in 0..rows)
                for (j in 0..columns)
                    if (this[i, j] != other[i, j])
                        return false
            return true
        }
    }

    override fun hashCode(): Int {
        var result = rows
        result = 31 * result + columns
        result = 31 * result + matrix.contentHashCode()
        return result
    }

    init {
        if (rows < 0 || columns < 0)
            throw IllegalArgumentException("Can not create negative matrix")
        if (rows == 0 || columns == 0)
            throw IllegalArgumentException("Can not create zero matrix")
    }
}

operator fun Number.times(b: Matrix): Matrix {
    val c = Matrix(b.rows, b.columns)
    for (i in 1..b.rows)
        for (j in 1..b.columns)
            c[i, j] = b[i, j] * this.toDouble()
    return c
}