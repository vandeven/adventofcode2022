fun main() {
    data class Elf(val number : Int, val calories : Int)

    val elves = "/day1.txt".readFile().fold(listOf(Elf(0, 0))) { acc, e ->
        if (e.isBlank()) {
            acc.plus(listOf(Elf(acc.last().number + 1, 0)))
        } else {
            acc.plus(acc.last().copy(calories = e.toInt()))
        }
    }.groupBy { it.number }
        .mapValues { it.value.map { it.calories }.sum() }

    println(elves.maxBy { it.value }.value)

    println(elves.values.sortedDescending().take(3).sum())
}