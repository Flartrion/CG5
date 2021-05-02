import java.awt.Dimension
import java.awt.GridLayout
import java.lang.Exception
import javax.swing.*

class PointChangeDialogue(owner: JFrame, onCompletion: (Vertex) -> Unit) : JDialog(owner, true) {
    private val xTextBox = JTextField()
    private val yTextBox = JTextField()
    private val zTextBox = JTextField()
    private val cancelButton = JButton("Отмена")
    private val confirmButton = JButton("Подтвердить")

    init {
        this.preferredSize = Dimension(400, 400)
        this.layout = GridLayout(4, 2)
        this.title = "Точка"
        this.isResizable = true

        this.add(JLabel("X: ", 0))
        this.add(xTextBox)

        this.add(JLabel("Y: ", 0))
        this.add(yTextBox)

        this.add(JLabel("Z: ", 0))
        this.add(zTextBox)

        confirmButton.addActionListener {
            print(it.actionCommand)
            try {
                onCompletion(
                    Vertex(
                        xTextBox.text.toDouble(),
                        yTextBox.text.toDouble(),
                        zTextBox.text.toDouble()
                    )
                )
                this.dispose()
            } catch (e: Exception) {

            }
        }
        cancelButton.addActionListener {
            print(it.actionCommand)

            this.dispose()
        }

        this.add(confirmButton)
        this.add(cancelButton)

        this.pack()
        this.isVisible = true
        this.setLocationRelativeTo(null)


    }
}