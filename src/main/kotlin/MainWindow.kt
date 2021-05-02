import java.awt.BorderLayout
import java.awt.GridLayout
import java.awt.Polygon
import javax.swing.*

class MainWindow : JFrame() {
    private val pointData = DefaultListModel<Vertex>()
    private val pointList = JList<Vertex>()
    private val bezierSurface = BezierSurface()
    private val pointEditButton = JButton("Изменить")

    init {
        pointData.addElement(Vertex(-150.0, 0.0, 150.0))
        pointData.addElement(Vertex(-150.0, 5.0, 50.0))
        pointData.addElement(Vertex(-150.0, 5.0, -50.0))
        pointData.addElement(Vertex(-150.0, 0.0, -150.0))

        pointData.addElement(Vertex(-50.0, 50.0, 150.0))
        pointData.addElement(Vertex(-50.0, 50.0, 50.0))
        pointData.addElement(Vertex(-50.0, 50.0, -50.0))
        pointData.addElement(Vertex(-50.0, 50.0, -150.0))

        pointData.addElement(Vertex(50.0, 50.0, 150.0))
        pointData.addElement(Vertex(50.0, 50.0, 50.0))
        pointData.addElement(Vertex(50.0, 50.0, -50.0))
        pointData.addElement(Vertex(50.0, 50.0, -150.0))

        pointData.addElement(Vertex(150.0, 0.0, 150.0))
        pointData.addElement(Vertex(150.0, 50.0, 50.0))
        pointData.addElement(Vertex(150.0, 50.0, -50.0))
        pointData.addElement(Vertex(150.0, 0.0, -150.0))

        pointList.model = pointData
        pointList.selectionMode = ListSelectionModel.SINGLE_SELECTION

        pointEditButton.addActionListener {
            if (pointList.selectedValue != null)
                PointChangeDialogue(this) {
                    pointData[pointList.selectedIndex] = it
                }
            bezierSurface.contourPoints.clear()
            for (i in 0 until pointData.size()) {
                bezierSurface.contourPoints.add(pointData[i])
            }
            bezierSurface.calculatePoints()
            repaint()
        }

        val topLabels = JPanel()
        topLabels.layout = GridLayout(1, 2)
        topLabels.add(JLabel("Точки поверхности", 0))
        topLabels.add(JLabel("Повороты", 0))

        val inputZone = JPanel()
        inputZone.layout = GridLayout(1, 2)

        for (i in 0 until pointData.size()) {
            bezierSurface.contourPoints.add(pointData[i])
        }
        bezierSurface.calculatePoints()

        this.defaultCloseOperation = EXIT_ON_CLOSE
        this.title = "Smooth criminal"
        this.isResizable = true
        this.add(bezierSurface, BorderLayout.CENTER)
        this.pack()
        this.setLocationRelativeTo(null)
        this.isVisible = true
    }
}