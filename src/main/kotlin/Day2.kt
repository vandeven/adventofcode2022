sealed class Hand(val points : Int){
    abstract fun scoreVs(other : Hand) : Int
    abstract fun getCorrectHand(other : Hand) : Hand
}
class Rock : Hand(1) {
    override fun scoreVs(other : Hand) =
        when(other){
            is Rock -> 3
            is Paper -> 0
            is Scissors -> 6
        }

    override fun getCorrectHand(other: Hand) = when(other){
        is Rock -> Scissors()
        is Paper -> Rock()
        is Scissors -> Paper()
    }
}
class Paper : Hand(2) {
    override fun scoreVs(other : Hand) =
        when(other){
            is Rock -> 6
            is Paper -> 3
            is Scissors -> 0
        }

    override fun getCorrectHand(other: Hand) = other
}
class Scissors : Hand(3){
    override fun scoreVs(other : Hand) =
        when(other){
            is Rock -> 0
            is Paper -> 6
            is Scissors -> 3
        }

    override fun getCorrectHand(other: Hand) = when(other){
        is Rock -> Paper()
        is Paper -> Scissors()
        is Scissors -> Rock()
    }
}

fun main() {
    fun String.translate() = when(this){
        "X" -> Rock()
        "Y" -> Paper()
        "Z" -> Scissors()
        "A" -> Rock()
        "B" -> Paper()
        "C" -> Scissors()
        else -> null
    }
    data class Game(val opponent : Hand, val player : Hand)
    val readFile = "/day2.txt".readFile()
        .map { it.split(" ") }
        .map { it.mapNotNull { hand -> hand.translate() } }
        .map { Game(it[0], it[1]) }

    fun calculateScore(game : Game) = game.player.scoreVs((game.opponent)) + game.player.points

    println(readFile.map(::calculateScore).sum())
    println(readFile.map { it.copy(player = it.player.getCorrectHand(it.opponent)) }.map (::calculateScore).sum())
}