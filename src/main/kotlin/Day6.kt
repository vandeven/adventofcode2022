fun main() {
    val result = "/day6.txt".readFile()
        .flatMap { it.toList() }
        .windowed(4, 1)
        .mapIndexed { i, e -> i to e }
        .filter { it.second.groupBy { it }.size == 4 }
        .map { it.first }
        .map { it + 4 }
        .first()

    println(result)

    val result2 = "/day6.txt".readFile()
        .flatMap { it.toList() }
        .windowed(14, 1)
        .mapIndexed { i, e -> i to e }
        .filter { it.second.groupBy { it }.size ==14 }
        .map { it.first }
        .map { it + 14 }
        .first()

    println(result2)
}