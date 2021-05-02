import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JPanel
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.roundToInt
import kotlin.math.sqrt

class BezierSurface : JPanel() {
    val contourPoints = ArrayList<Vertex>()

    private val bezierCurvePoints = ArrayList<Vertex>()

    init {
        preferredSize = Dimension(640, 640)
    }

    fun calculatePoints() {
        bezierCurvePoints.clear()
        repeat(441) {
            bezierCurvePoints.add(Vertex(0.0, 0.0, 0.0))
        }
        for (u in 0..100 step 5) {
            for (v in 0..100 step 5) {
                val index = v / 5 + u / 5 * 21
                for (i in 0..3) {
                    for (j in 0..3) {
                        bezierCurvePoints[index] += contourPoints[j + 4 * i] *
                                SurfaceMath.bernsteinPoly(3, i, u.toDouble() / 100) *
                                SurfaceMath.bernsteinPoly(3, j, v.toDouble() / 100)
                    }
                }
            }
        }
    }

    private fun drawFigureFrom(g: Graphics2D, p1: Vertex, p2: Vertex) {
        val axis = p2 - p1
        val phi = atan2(axis.y, axis.x)
        val theta = atan2(sqrt(axis.x * axis.x + axis.y * axis.y), axis.z)
        val fov = sqrt(axis.x * axis.x + axis.y * axis.y + axis.z * axis.z)

        val objectMatrix = Matrix(contourPoints.size + bezierCurvePoints.size + 4, 4)
        for (i in contourPoints.indices) {
            objectMatrix[i + 1, 1] = contourPoints[i].x
            objectMatrix[i + 1, 2] = contourPoints[i].y
            objectMatrix[i + 1, 3] = contourPoints[i].z
            objectMatrix[i + 1, 4] = 1.0
        }

        for (i in bezierCurvePoints.indices) {
            objectMatrix[i + 1 + contourPoints.size, 1] = bezierCurvePoints[i].x
            objectMatrix[i + 1 + contourPoints.size, 2] = bezierCurvePoints[i].y
            objectMatrix[i + 1 + contourPoints.size, 3] = bezierCurvePoints[i].z
            objectMatrix[i + 1 + contourPoints.size, 4] = 1.0
        }

        objectMatrix[contourPoints.size + bezierCurvePoints.size + 1, 1] = 0.0
        objectMatrix[contourPoints.size + bezierCurvePoints.size + 1, 2] = 0.0
        objectMatrix[contourPoints.size + bezierCurvePoints.size + 1, 3] = 0.0
        objectMatrix[contourPoints.size + bezierCurvePoints.size + 1, 4] = 1.0
        objectMatrix[contourPoints.size + bezierCurvePoints.size + 2, 1] = 100.0
        objectMatrix[contourPoints.size + bezierCurvePoints.size + 2, 2] = 0.0
        objectMatrix[contourPoints.size + bezierCurvePoints.size + 2, 3] = 0.0
        objectMatrix[contourPoints.size + bezierCurvePoints.size + 2, 4] = 1.0
        objectMatrix[contourPoints.size + bezierCurvePoints.size + 3, 1] = 0.0
        objectMatrix[contourPoints.size + bezierCurvePoints.size + 3, 2] = 100.0
        objectMatrix[contourPoints.size + bezierCurvePoints.size + 3, 3] = 0.0
        objectMatrix[contourPoints.size + bezierCurvePoints.size + 3, 4] = 1.0
        objectMatrix[contourPoints.size + bezierCurvePoints.size + 4, 1] = 0.0
        objectMatrix[contourPoints.size + bezierCurvePoints.size + 4, 2] = 0.0
        objectMatrix[contourPoints.size + bezierCurvePoints.size + 4, 3] = 100.0
        objectMatrix[contourPoints.size + bezierCurvePoints.size + 4, 4] = 1.0


        val preparationTransform = TransformMatrixFabric.translate(-p1.x, -p1.y, -p1.z) *
                TransformMatrixFabric.rotateZ(-phi) *
                TransformMatrixFabric.rotateY(-theta) *
                TransformMatrixFabric.rotateZ(PI / 2)

        val resultMatrix = objectMatrix * preparationTransform * TransformMatrixFabric.scale(
            1.0,
            1.0,
            0.0
        )
        g.translate(100, 100)
        g.color = Color.GRAY
        for (i in (contourPoints.size + bezierCurvePoints.size + 2)..(contourPoints.size + bezierCurvePoints.size + 4)) {
            val xy1 = Vertex(
                resultMatrix[(contourPoints.size + bezierCurvePoints.size + 1), 1],
                resultMatrix[(contourPoints.size + bezierCurvePoints.size + 1), 2],
                resultMatrix[(contourPoints.size + bezierCurvePoints.size + 1), 3]
            )
            val xy2 = Vertex(resultMatrix[i, 1], resultMatrix[i, 2], resultMatrix[i, 3])
            g.drawLine(
                xy1.x.roundToInt(), xy1.y.roundToInt(),
                xy2.x.roundToInt(), xy2.y.roundToInt()
            )
        }

        g.translate(-100, -100)
        g.translate(width / 2, height / 2)

        g.color = Color.WHITE
        for (i in 0 until 4) {
            for (j in 0 until 3) {
                val xy1 = Vertex(
                    resultMatrix[j + i * 4 + 1, 1],
                    resultMatrix[j + i * 4 + 1, 2],
                    resultMatrix[j + i * 4 + 1, 3]
                )
                val xy2 = Vertex(
                    resultMatrix[j + i * 4 + 2, 1],
                    resultMatrix[j + i * 4 + 2, 2],
                    resultMatrix[j + i * 4 + 2, 3]
                )
                g.drawLine(
                    xy1.x.roundToInt(), xy1.y.roundToInt(),
                    xy2.x.roundToInt(), xy2.y.roundToInt()
                )
            }
        }

        for (j in 0 until 4) {
            for (i in 0 until 3) {
                val xy1 = Vertex(
                    resultMatrix[j + i * 4 + 1, 1],
                    resultMatrix[j + i * 4 + 1, 2],
                    resultMatrix[j + i * 4 + 1, 3]
                )
                val xy2 = Vertex(
                    resultMatrix[j + (i + 1) * 4 + 1, 1],
                    resultMatrix[j + (i + 1) * 4 + 1, 2],
                    resultMatrix[j + (i + 1) * 4 + 1, 3]
                )
                g.drawLine(
                    xy1.x.roundToInt(), xy1.y.roundToInt(),
                    xy2.x.roundToInt(), xy2.y.roundToInt()
                )
            }
        }

        g.color = Color.CYAN
//        for (i in contourPoints.size until contourPoints.size + bezierCurvePoints.size - 1) {
//            val xy1 = Vertex(resultMatrix[i + 1, 1], resultMatrix[i + 1, 2], resultMatrix[i + 1, 3])
//            val xy2 = Vertex(resultMatrix[i + 2, 1], resultMatrix[i + 2, 2], resultMatrix[i + 2, 3])
//            g.drawLine(
//                xy1.x.roundToInt(), xy1.y.roundToInt(),
//                xy2.x.roundToInt(), xy2.y.roundToInt()
//            )
//        }

        for (i in 0 until 21) {
            for (j in 0 until 20) {
                val xy1 = Vertex(
                    resultMatrix[contourPoints.size + j + i * 21 + 1, 1],
                    resultMatrix[contourPoints.size + j + i * 21 + 1, 2],
                    resultMatrix[contourPoints.size + j + i * 21 + 1, 3]
                )
                val xy2 = Vertex(
                    resultMatrix[contourPoints.size + j + i * 21 + 2, 1],
                    resultMatrix[contourPoints.size + j + i * 21 + 2, 2],
                    resultMatrix[contourPoints.size + j + i * 21 + 2, 3]
                )
//                val xy1 = Vertex(
//                    resultMatrix[contourPoints.size + j * 21 + i + 1, 1],
//                    resultMatrix[contourPoints.size + j * 21 + i + 1, 2],
//                    resultMatrix[contourPoints.size + j * 21 + i + 1, 3]
//                )
//                val xy2 = Vertex(
//                    resultMatrix[contourPoints.size + (j+1) * 21 + i + 1, 1],
//                    resultMatrix[contourPoints.size + (j+1) * 21 + i + 1, 2],
//                    resultMatrix[contourPoints.size + (j+1) * 21 + i + 1, 3]
//                )
                g.drawLine(
                    xy1.x.roundToInt(), xy1.y.roundToInt(),
                    xy2.x.roundToInt(), xy2.y.roundToInt()
                )
            }
        }

        for (j in 0 until 21) {
            for (i in 0 until 20) {
                val xy1 = Vertex(
                    resultMatrix[contourPoints.size + j + i * 21 + 1, 1],
                    resultMatrix[contourPoints.size + j + i * 21 + 1, 2],
                    resultMatrix[contourPoints.size + j + i * 21 + 1, 3]
                )
                val xy2 = Vertex(
                    resultMatrix[contourPoints.size + j + (i + 1) * 21 + 1, 1],
                    resultMatrix[contourPoints.size + j + (i + 1) * 21 + 1, 2],
                    resultMatrix[contourPoints.size + j + (i + 1) * 21 + 1, 3]
                )
//                val xy1 = Vertex(
//                    resultMatrix[contourPoints.size + j * 21 + i + 1, 1],
//                    resultMatrix[contourPoints.size + j * 21 + i + 1, 2],
//                    resultMatrix[contourPoints.size + j * 21 + i + 1, 3]
//                )
//                val xy2 = Vertex(
//                    resultMatrix[contourPoints.size + (j) * 21 + i + 2, 1],
//                    resultMatrix[contourPoints.size + (j) * 21 + i + 2, 2],
//                    resultMatrix[contourPoints.size + (j) * 21 + i + 2, 3]
//                )
                g.drawLine(
                    xy1.x.roundToInt(), xy1.y.roundToInt(),
                    xy2.x.roundToInt(), xy2.y.roundToInt()
                )
            }
        }
    }

    fun rotateOnX(angle: Double) {
        var transformBezierPoints = Matrix(bezierCurvePoints.size, 4)
        var transformContourPoints = Matrix(contourPoints.size, 4)
        for (i in 1..bezierCurvePoints.size) {
            transformBezierPoints[i, 1] = bezierCurvePoints[i - 1].x
            transformBezierPoints[i, 2] = bezierCurvePoints[i - 1].y
            transformBezierPoints[i, 3] = bezierCurvePoints[i - 1].z
            transformBezierPoints[i, 4] = 1.0
        }
        for (i in 1..contourPoints.size) {
            transformContourPoints[i, 1] = contourPoints[i - 1].x
            transformContourPoints[i, 2] = contourPoints[i - 1].y
            transformContourPoints[i, 3] = contourPoints[i - 1].z
            transformContourPoints[i, 4] = 1.0
        }

        transformBezierPoints *= TransformMatrixFabric.rotateX(angle / 180 * PI)
        transformContourPoints *= TransformMatrixFabric.rotateX(angle / 180 * PI)

        for (i in 1..bezierCurvePoints.size) {
            bezierCurvePoints[i - 1].x = transformBezierPoints[i, 1]
            bezierCurvePoints[i - 1].y = transformBezierPoints[i, 2]
            bezierCurvePoints[i - 1].z = transformBezierPoints[i, 3]
        }
        for (i in 1..contourPoints.size) {
            contourPoints[i - 1].x = transformContourPoints[i, 1]
            contourPoints[i - 1].y = transformContourPoints[i, 2]
            contourPoints[i - 1].z = transformContourPoints[i, 3]
        }
    }

    fun rotateOnY(angle: Double) {
        var transformBezierPoints = Matrix(bezierCurvePoints.size, 4)
        var transformContourPoints = Matrix(contourPoints.size, 4)
        for (i in 1..bezierCurvePoints.size) {
            transformBezierPoints[i, 1] = bezierCurvePoints[i - 1].x
            transformBezierPoints[i, 2] = bezierCurvePoints[i - 1].y
            transformBezierPoints[i, 3] = bezierCurvePoints[i - 1].z
            transformBezierPoints[i, 4] = 1.0
        }
        for (i in 1..contourPoints.size) {
            transformContourPoints[i, 1] = contourPoints[i - 1].x
            transformContourPoints[i, 2] = contourPoints[i - 1].y
            transformContourPoints[i, 3] = contourPoints[i - 1].z
            transformContourPoints[i, 4] = 1.0
        }

        transformBezierPoints *= TransformMatrixFabric.rotateY(angle / 180 * PI)
        transformContourPoints *= TransformMatrixFabric.rotateY(angle / 180 * PI)

        for (i in 1..bezierCurvePoints.size) {
            bezierCurvePoints[i - 1].x = transformBezierPoints[i, 1]
            bezierCurvePoints[i - 1].y = transformBezierPoints[i, 2]
            bezierCurvePoints[i - 1].z = transformBezierPoints[i, 3]
        }
        for (i in 1..contourPoints.size) {
            contourPoints[i - 1].x = transformContourPoints[i, 1]
            contourPoints[i - 1].y = transformContourPoints[i, 2]
            contourPoints[i - 1].z = transformContourPoints[i, 3]
        }
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)

        val gg = g as Graphics2D
        background = Color.BLACK
        drawFigureFrom(gg, Vertex(50.0, 50.0, 50.0), Vertex(40.0, 40.0, 40.0))
    }
}