package main
import javafx.application.Platform
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
    override val root = javafx.scene.layout.GridPane()
    var theWorld: World
    val padding = 2.0
    val size = 30
    val speed = 1000L

    init {
        theWorld = World(size)
        root.hgap = padding
        root.vgap = padding
        theWorld.worldState[2][2].state = true
        theWorld.worldState[3][3].state = true
        theWorld.worldState[1][4].state = true
        theWorld.worldState[2][4].state = true
        theWorld.worldState[3][4].state = true

        drawWorld()
        thread {
            val timer = Timer("mainloop", true)
            timer.scheduleAtFixedRate(0, speed) {
                theWorld.iterate()
                Platform.runLater({
                    drawWorld()
                })
            }
        }
    }

    fun drawWorld(){
        theWorld.processWorld({ xPos, yPos ->
            val state = theWorld.worldState[xPos][yPos].state
            val rectangle = Rectangle(10.0,10.0, if (state) Color.RED else Color.BLUE)
            root.add(rectangle, xPos, yPos)
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
