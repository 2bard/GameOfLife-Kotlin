package main

/**
 * Created by Lewis on 06/02/2018.
 */
class World(size: Int){
    val size: Int = size
    var worldState: Array<Array<Cell>> = blankWorld()

    fun blankWorld(): Array<Array<Cell>>{
        return Array(size){
            Array(size, {
                Cell(false)
            })
        }
    }

    fun processWorld(funcX: (Int, Int) -> Unit, funcY: () -> Unit){
        var x = 0
        var y = 0
        worldState.forEach{
            it.forEach{
                funcX.invoke(x,y)
                x++
            }
            funcY.invoke()
            y++
            x = 0
        }
    }

    fun iterate(){
        this.processWorld({ xPos, yPos -> worldState[xPos][yPos].initialise(xPos,yPos,this) }, {})
        var newWorld = blankWorld()
        this.processWorld({ xPos, yPos -> newWorld[xPos][yPos].state = neighbours(xPos, yPos) }, {})
        this.worldState = newWorld
    }

    fun neighbours(xPos: Int, yPos: Int): Boolean{
        var count = worldState[xPos][yPos].neighbours.filter { it.state }.count()
        if (count < 2) return false
        else if (count == 2) return worldState[xPos][yPos].state
        else return (count == 3)
    }

    fun neighbourExists(pos: Pair<Int,Int>): Boolean =
            (pos.first in 0..worldState.size - 1 && pos.second in 0..worldState[pos.first].size - 1)

    fun flipState(xPos: Int, yPos: Int){
        worldState[xPos][yPos].state = !worldState[xPos][yPos].state
    }

    class Cell(initialState: Boolean){
        var state = initialState
        var neighbours = emptyList<Cell>()

        fun initialise(xPos: Int, yPos: Int, world: World) {
            neighbours = arrayListOf(
                    Pair(xPos -1, yPos -1), Pair(xPos, yPos - 1), Pair(xPos + 1, yPos - 1), Pair(xPos - 1, yPos),
                    Pair(xPos + 1, yPos), Pair(xPos - 1, yPos + 1), Pair(xPos, yPos + 1), Pair(xPos + 1, yPos + 1)
            ).filter { world.neighbourExists(it) }.map { world.worldState[it.first][it.second] }
        }
    }
}