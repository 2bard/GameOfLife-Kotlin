package main
import javafx.application.Platform
import javafx.scene.control.Button
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import tornadofx.App
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import tornadofx.View
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate
import kotlin.concurrent.thread

/**
 * Created by Lewis on 06/02/2017.
 */

class GameOfLife : View() {
    override val root = VBox()
    val grid = GridPane()
    var theWorld: World
    val padding = 2.0
    val size = 30
    val speed = 500L
    var play = false
    var button = Button("Start")

    init {
        button.setOnMouseClicked {
            if(play) {
                this.play = false
                button.text = "Start"
            }
            else {
                this.play = true
                button.text = "Stop"
            }
        }

        root.add(grid)
        root.add(button)

        theWorld = World(size)
        grid.hgap = padding
        grid.vgap = padding
        theWorld.worldState[2][2].state = true
        theWorld.worldState[3][3].state = true
        theWorld.worldState[1][4].state = true
        theWorld.worldState[2][4].state = true
        theWorld.worldState[3][4].state = true

        drawWorld()
        thread {
            val timer = Timer("mainloop", true)
            timer.scheduleAtFixedRate(0, speed) {
                if(play){
                    theWorld.iterate()
                    Platform.runLater({
                        drawWorld()
                    })
                }
            }
        }
    }

    fun drawWorld(){
        theWorld.processWorld({ xPos, yPos ->
            val state = theWorld.worldState[xPos][yPos].state
            val rectangle = Rectangle(10.0,10.0, if (state) Color.RED else Color.BLUE)
            grid.add(rectangle, xPos, yPos)
            rectangle.setOnMouseClicked {
                theWorld.flipState(xPos,yPos)
                drawWorld()
            }
        }, {})
    }
}

class GameOfLifeApp : App() {
    override val primaryView = GameOfLife::class
}
