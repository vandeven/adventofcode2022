fun main() {
    val result = "/day4.txt".readFile()
        .map {
            it.split(",")
                .map {
                    val splitted = it.split("-")
                        .map { it.toInt() }
                    (splitted[0]..splitted[1]).toList()
                }

        }
        .filter { it[0].containsAll(it[1]) || it[1].containsAll(it[0])}
        .size

    println(result)

    val result2 = "/day4.txt".readFile()
        .map {
            it.split(",")
                .map {
                    val splitted = it.split("-")
                        .map { it.toInt() }
                    (splitted[0]..splitted[1])
                }

        }
        .filter { it[0].intersect(it[1]).isNotEmpty() }
        .size

    println(result2)

}