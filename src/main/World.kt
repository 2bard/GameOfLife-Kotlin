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

    fun print(){
        this.processWorld({ xPos, yPos ->
            print(if (worldState[xPos][yPos].state) "O" else "X")
        }, { println() })
    }

    fun processWorld(funcX: (Int, Int) -> Unit, funcY: () -> Unit){
        var x = 0
        var y = 0
        worldState.map{
            it.map{
                funcX.invoke(x,y)
                x++
            }
            funcY.invoke()
            y++
            x = 0
        }
    }

    fun iterate(){
        var newWorld = blankWorld()
        this.processWorld({ xPos, yPos ->
            newWorld[xPos][yPos].state = neighbours(xPos, yPos)
        }, {})

        this.worldState = newWorld;
    }

    fun checkNeighbour(xPos: Int, yPos: Int, results: ArrayList<Cell>){
        if(neighbourExists(xPos, yPos) && worldState[xPos][yPos].state){
            results.add(worldState[xPos][yPos])
        }
    }

    fun neighbours(xPos: Int, yPos: Int): Boolean{

        var result = ArrayList<Cell>()

        checkNeighbour(xPos - 1, yPos - 1, result)
        checkNeighbour(xPos, yPos - 1, result)
        checkNeighbour(xPos + 1, yPos - 1, result)
        checkNeighbour(xPos - 1, yPos, result)
        checkNeighbour(xPos + 1, yPos, result)
        checkNeighbour(xPos - 1, yPos + 1, result)
        checkNeighbour(xPos, yPos + 1, result)
        checkNeighbour(xPos + 1, yPos + 1, result)

        if (result.size < 2)
            return false
        else if (result.size == 2)
            return worldState[xPos][yPos].state
        else
            return (result.size == 3)
    }

    fun neighbourExists(xPos: Int, yPos: Int): Boolean{
        if(xPos < 0 || xPos >= worldState.size)
            return false
        else
            return !(yPos < 0 || yPos >= worldState[xPos].size)
    }

    fun flipState(xPos: Int, yPos: Int){
        worldState[xPos][yPos].state = !worldState[xPos][yPos].state
    }

    class Cell(initialState: Boolean){
        var state = initialState
    }
}