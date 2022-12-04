fun main() {

    data class Rucksack(val compartment1 : List<Char>, val compartment2 : List<Char>)

    val result = "/day3.txt".readFile()
        .map { it.chunked(it.length / 2) }
        .map { Rucksack(it[0].toList(), it[1].toList()) }
        .flatMap { it.compartment1.intersect(it.compartment2) }
        .map { it.code - if(it.isUpperCase()) {38} else {96} }
        .sum()

    println(result)

    val result2 = "/day3.txt".readFile()
        .windowed(3, 3)
        .map { it.map { it.toList() } }
        .flatMap { it[0].intersect(it[1]).intersect(it[2]) }
        .map { it.code - if(it.isUpperCase()) {38} else {96} }
        .sum()
    println(result2)
}