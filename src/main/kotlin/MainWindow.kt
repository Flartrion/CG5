import java.awt.BorderLayout
import java.awt.GridLayout
import javax.swing.*

class MainWindow : JFrame() {
    private val pointData = DefaultListModel<Vertex>()
    private val pointList = JList<Vertex>()
    private val bezierSurface = BezierSurface()
    private val pointEditButton = JButton("Изменить")

    init {
        val inputPanel = JPanel()
        inputPanel.layout = BorderLayout()

        val pointInput = JPanel()
        pointInput.layout = BoxLayout(pointInput, BoxLayout.PAGE_AXIS)

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

        pointInput.add(JScrollPane(pointList))
        pointInput.add(pointEditButton)

        val rotations = JPanel()
        rotations.layout = GridLayout(2, 3)

        val rotationX = JTextField("5")
        val rotationY = JTextField("5")
        val rotationXСonfirm = JButton("Повернуть")
        val rotationYСonfirm = JButton("Повернуть")

        rotationXСonfirm.addActionListener {
            bezierSurface.rotateOnX(rotationX.text.toDouble())
            repaint()
        }
        rotationYСonfirm.addActionListener {
            bezierSurface.rotateOnY(rotationY.text.toDouble())
            repaint()
        }

        rotations.add(JLabel("X: ", 0))
        rotations.add(rotationX)
        rotations.add(rotationXСonfirm)
        rotations.add(JLabel("Y: ", 0))
        rotations.add(rotationY)
        rotations.add(rotationYСonfirm)

        val topLabels = JPanel()
        topLabels.layout = GridLayout(1, 2)
        topLabels.add(JLabel("Точки поверхности", 0))
        topLabels.add(JLabel("Повороты", 0))

        val inputZone = JPanel()
        inputZone.layout = GridLayout(1, 2)
        inputZone.add(pointInput)
        inputZone.add(rotations)

        inputPanel.add(topLabels, BorderLayout.NORTH)
        inputPanel.add(inputZone, BorderLayout.SOUTH)

        for (i in 0 until pointData.size()) {
            bezierSurface.contourPoints.add(pointData[i])
        }
        bezierSurface.calculatePoints()

        this.add(inputPanel, BorderLayout.NORTH)
        this.defaultCloseOperation = EXIT_ON_CLOSE
        this.title = "Smooth criminal"
        this.isResizable = true
        this.add(bezierSurface, BorderLayout.CENTER)
        this.pack()
        this.setLocationRelativeTo(null)
        this.isVisible = true
    }
}