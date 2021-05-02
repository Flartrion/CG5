import java.awt.*
import javax.swing.JPanel
import kotlin.math.*

class BezierSurface : JPanel() {
    val points = ArrayList<Vertex>()
    val polys = ArrayList<Triple<Int, Int, Int>>()

//    private val bezierCurvePoints = ArrayList<Vertex>()

    init {
        preferredSize = Dimension(640, 640)
        points.add(Vertex(0.0, 0.0, -100.0));
        val change = sqrt(5.0) / 2
        for (i in 0..4) {
            points.add(
                Vertex(
                    sin(change) * cos(2 * i * PI / 5) * -100,
                    sin(change) * sin(2 * i * Math.PI / 5) * -100,
                    cos(change) * -100
                )
            )
        }
        for (i in 0..4) {
            points.add(
                Vertex(
                    sin(change) * cos(2 * i * Math.PI / 5) * 100,
                    sin(change) * sin(2 * i * Math.PI / 5) * 100,
                    cos(change) * 100
                )
            )
        }
        points.add(Vertex(0.0, 0.0, 100.0))

        for (i in 0..4) {
            polys.add(Triple(0, (i + 1), ((i + 1) % 5 + 1)))
        }
        for (i in 0..4) {
            polys.add(Triple((i + 1), ((i + 2) % 5 + 6), ((i + 3) % 5 + 6)))
        }
        for (i in 0..4) {
            polys.add(Triple((i + 6), ((i + 2) % 5 + 1), ((i + 3) % 5 + 1)))
        }
        for (i in 0..4) {
            polys.add(Triple(11, ((i + 2) % 5 + 6), ((i + 3) % 5 + 6)))
        }
    }

//    fun calculatePoints() {
//        bezierCurvePoints.clear()
//        repeat(441) {
//            bezierCurvePoints.add(Vertex(0.0, 0.0, 0.0))
//        }
//        for (u in 0..100 step 5) {
//            for (v in 0..100 step 5) {
//                val index = v / 5 + u / 5 * 21
//                for (i in 0..3) {
//                    for (j in 0..3) {
//                        bezierCurvePoints[index] += contourPoints[j + 4 * i] *
//                                SurfaceMath.bernsteinPoly(3, i, u.toDouble() / 100) *
//                                SurfaceMath.bernsteinPoly(3, j, v.toDouble() / 100)
//                    }
//                }
//            }
//        }
//    }

    private fun drawFigureFrom(g: Graphics2D, p1: Vertex, p2: Vertex) {
        val axis = p2 - p1
        val phi = atan2(axis.y, axis.x)
        val theta = atan2(sqrt(axis.x * axis.x + axis.y * axis.y), axis.z)
        val fov = sqrt(axis.x * axis.x + axis.y * axis.y + axis.z * axis.z)

        val objectMatrix = Matrix(points.size + 4, 4)
        for (i in points.indices) {
            objectMatrix[i + 1, 1] = points[i].x
            objectMatrix[i + 1, 2] = points[i].y
            objectMatrix[i + 1, 3] = points[i].z
            objectMatrix[i + 1, 4] = 1.0
        }

        objectMatrix[points.size + 1, 1] = 0.0
        objectMatrix[points.size + 1, 2] = 0.0
        objectMatrix[points.size + 1, 3] = 0.0
        objectMatrix[points.size + 1, 4] = 1.0
        objectMatrix[points.size + 2, 1] = 100.0
        objectMatrix[points.size + 2, 2] = 0.0
        objectMatrix[points.size + 2, 3] = 0.0
        objectMatrix[points.size + 2, 4] = 1.0
        objectMatrix[points.size + 3, 1] = 0.0
        objectMatrix[points.size + 3, 2] = 100.0
        objectMatrix[points.size + 3, 3] = 0.0
        objectMatrix[points.size + 3, 4] = 1.0
        objectMatrix[points.size + 4, 1] = 0.0
        objectMatrix[points.size + 4, 2] = 0.0
        objectMatrix[points.size + 4, 3] = 100.0
        objectMatrix[points.size + 4, 4] = 1.0


        val preparationTransform = TransformMatrixFabric.translate(-p1.x, -p1.y, -p1.z) *
                TransformMatrixFabric.rotateZ(-phi) *
                TransformMatrixFabric.rotateY(-theta) *
                TransformMatrixFabric.rotateZ(PI / 2)

        val resultMatrix = objectMatrix * preparationTransform * TransformMatrixFabric.scale(
            1.0,
            1.0,
            0.0
        )

        val normals = ArrayList<Vertex>()
        val isVisible = ArrayList<Boolean>()

        for (i in polys.indices) {
            normals.add(
                VectorMath.crossProduct(
                    points[polys[i].second] - points[polys[i].first],
                    points[polys[i].third] - points[polys[i].first]
                )
            )
            if (VectorMath.angleBetween(normals[i], points[polys[i].first]) > PI / 2) {
                normals[i] = -normals[i]
            }
            isVisible.add(VectorMath.angleBetween(normals[i], p2 - p1) > PI / 2)
        }




        g.translate(100, 100)
        g.color = Color.GRAY
        for (i in (points.size + 2)..(points.size + 4)) {
            val xy1 = Vertex(
                resultMatrix[(points.size + 1), 1],
                resultMatrix[(points.size + 1), 2],
                resultMatrix[(points.size + 1), 3]
            )
            val xy2 = Vertex(resultMatrix[i, 1], resultMatrix[i, 2], resultMatrix[i, 3])
            g.drawLine(
                xy1.x.roundToInt(), xy1.y.roundToInt(),
                xy2.x.roundToInt(), xy2.y.roundToInt()
            )
        }

        g.translate(-100, -100)
        g.translate(width / 2, height / 2)

        for (i in polys.indices) {
            if (isVisible[i]) {
                val xy1 = Vertex(
                    resultMatrix[polys[i].first + 1, 1],
                    resultMatrix[polys[i].first + 1, 2],
                    resultMatrix[polys[i].first + 1, 3]
                )
                val xy2 = Vertex(
                    resultMatrix[polys[i].second + 1, 1],
                    resultMatrix[polys[i].second + 1, 2],
                    resultMatrix[polys[i].second + 1, 3]
                )
                val xy3 = Vertex(
                    resultMatrix[polys[i].third + 1, 1],
                    resultMatrix[polys[i].third + 1, 2],
                    resultMatrix[polys[i].third + 1, 3]
                )
                val xArray = IntArray(3)
                val yArray = IntArray(3)
                xArray[0] = resultMatrix[polys[i].first + 1, 1].roundToInt()
                xArray[1] = resultMatrix[polys[i].second + 1, 1].roundToInt()
                xArray[2] = resultMatrix[polys[i].third + 1, 1].roundToInt()
                yArray[0] = resultMatrix[polys[i].first + 1, 2].roundToInt()
                yArray[1] = resultMatrix[polys[i].second + 1, 2].roundToInt()
                yArray[2] = resultMatrix[polys[i].third + 1, 2].roundToInt()

                g.color = Color.GRAY
                g.fillPolygon(Polygon(xArray, yArray, 3))

                g.color = Color.WHITE
                g.drawLine(
                    xy1.x.roundToInt(), xy1.y.roundToInt(),
                    xy2.x.roundToInt(), xy2.y.roundToInt()
                )
                g.drawLine(
                    xy1.x.roundToInt(), xy1.y.roundToInt(),
                    xy3.x.roundToInt(), xy3.y.roundToInt()
                )
                g.drawLine(
                    xy2.x.roundToInt(), xy2.y.roundToInt(),
                    xy3.x.roundToInt(), xy3.y.roundToInt()
                )
            }
        }

//        for (i in contourPoints.size until contourPoints.size - 1) {
//            val xy1 = Vertex(resultMatrix[i + 1, 1], resultMatrix[i + 1, 2], resultMatrix[i + 1, 3])
//            val xy2 = Vertex(resultMatrix[i + 2, 1], resultMatrix[i + 2, 2], resultMatrix[i + 2, 3])
//            g.drawLine(
//                xy1.x.roundToInt(), xy1.y.roundToInt(),
//                xy2.x.roundToInt(), xy2.y.roundToInt()
//            )
//        }
    }

    fun rotateOnX(angle: Double) {
        var transformContourPoints = Matrix(points.size, 4)
        for (i in 1..points.size) {
            transformContourPoints[i, 1] = points[i - 1].x
            transformContourPoints[i, 2] = points[i - 1].y
            transformContourPoints[i, 3] = points[i - 1].z
            transformContourPoints[i, 4] = 1.0
        }

        transformContourPoints *= TransformMatrixFabric.rotateX(angle / 180 * PI)

        for (i in 1..points.size) {
            points[i - 1].x = transformContourPoints[i, 1]
            points[i - 1].y = transformContourPoints[i, 2]
            points[i - 1].z = transformContourPoints[i, 3]
        }
    }

    fun rotateOnY(angle: Double) {
        var transformContourPoints = Matrix(points.size, 4)
        for (i in 1..points.size) {
            transformContourPoints[i, 1] = points[i - 1].x
            transformContourPoints[i, 2] = points[i - 1].y
            transformContourPoints[i, 3] = points[i - 1].z
            transformContourPoints[i, 4] = 1.0
        }

        transformContourPoints *= TransformMatrixFabric.rotateY(angle / 180 * PI)

        for (i in 1..points.size) {
            points[i - 1].x = transformContourPoints[i, 1]
            points[i - 1].y = transformContourPoints[i, 2]
            points[i - 1].z = transformContourPoints[i, 3]
        }
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)

        val gg = g as Graphics2D
        background = Color.BLACK
        drawFigureFrom(gg, Vertex(50.0, 50.0, 50.0), Vertex(40.0, 40.0, 40.0))
    }
}