fun main() {
    val boxes = "/day5.txt".readFile().takeWhile { it.isNotBlank() }

    val boxValues = boxes.dropLast(1).flatMap { it.mapIndexedNotNull{ i, e ->
        if(e.isUpperCase()){
            e to i
        } else {
            null
        }
    } }.groupBy ({ it.second }){it.first}

    val grid = boxes.last().mapIndexedNotNull{ i, e ->
        if(e.isDigit()){
            e to i
        } else {
            null
        }
    }.associate { it.first.digitToInt() to boxValues[it.second] }

    data class Move(val amount : Int, val from : Int, val to : Int)
    val regex = "move (\\d+) from (\\d+) to (\\d+)".toRegex()
    val commands = "/day5.txt".readFile().dropWhile { it.isNotBlank() }.drop(1).map {
        val groups = regex.matchEntire(it)!!.groupValues
        Move(groups[1].toInt(), groups[2].toInt(), groups[3].toInt())
    }
    val result = commands.fold(grid){ g, e ->
        val from = g[e.from]!!.take(e.amount)
        val to = g[e.to]!!
        val newTo = from.fold(to){ acc, f ->
            listOf(f).plus(acc.asSequence())
        }
        val newFrom = e.from to g[e.from]!!.drop(e.amount)
        g.plus(e.to to newTo).plus(newFrom)
    }.map { it.value!!.firstOrNull() ?: "" }.joinToString(separator = "")

    println(result)
}