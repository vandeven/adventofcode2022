sealed interface Command
data class CD(val name: String) : Command
data class LS(val values: List<Item>) : Command

sealed interface Item
data class File(val name: String, val size: Int) : Item
data class Directory(
    val name: String,
    val parent: String,
    val directories: Set<String>,
    val files: Set<File>,
    val size: Int = 0
) : Item


fun main() {
    val all = "/day7.txt".readFile()
    val commands = all.mapIndexed { i, e -> i to e }
        .filter { it.second.startsWith("$") }
        .map {
            if (it.second.startsWith("$ cd")) {
                CD(it.second.substring(5))
            } else {
                LS(all.subList(it.first + 1, all.size)
                    .takeWhile { !it.startsWith("$") }
                    .map {
                        if (it.startsWith("dir")) {
                            Directory(it.substring(4), "", setOf(), setOf())
                        } else {
                            File(it.substringAfter(" "), it.substringBefore(" ").toInt())
                        }
                    }
                )
            }
        }
    val allDirs = setOf(Directory("/", "", setOf(), setOf())).associateBy { it.name }

    data class Walker(val currentDir: Directory, val allDir: Map<String, Directory>)

    val newDirs = commands.drop(1)
        .fold(Walker(allDirs["/"]!!, allDirs)) { walker, e ->
            when (e) {
                is CD -> when (e.name) {
                    ".." -> walker.copy(currentDir = walker.allDir[walker.currentDir.parent]!!)
                    else -> {
                        walker.copy(currentDir = walker.allDir["${walker.currentDir.name}/${e.name}"]!!)
                    }
                }

                is LS -> {
                    val directories =
                        e.values.filterIsInstance<Directory>().map { "${walker.currentDir.name}/${it.name}" }.toSet()
                    val files = e.values.filterIsInstance<File>().toSet()
                    var newWalker = walker.copy(
                        allDir = walker.allDir.plus(
                            walker.currentDir.name to walker.currentDir.copy(
                                files = files,
                                directories = directories
                            )
                        )
                    )
                    directories.forEach {
                        newWalker = newWalker.copy(
                            allDir = newWalker.allDir.plus(
                                it to Directory(
                                    it,
                                    walker.currentDir.name,
                                    setOf(),
                                    setOf()
                                )
                            )
                        )
                    }
                    newWalker
                }
            }
        }.allDir.toMutableMap()


    fun calculateSize(directory: Directory): Int {
        val dirSize = directory.directories.map { newDirs[it]!! }.sumOf { calculateSize(it) }
        val filesSize = directory.files.map { it.size }.sum()
        newDirs[directory.name] = directory.copy(size = dirSize + filesSize)
        return dirSize + filesSize
    }

    calculateSize(newDirs["/"]!!)

    println(newDirs.values.filter { it.size < 100000 }.sumOf { it.size })

    val neededSpace = 30000000 - (70000000 - newDirs["/"]!!.size)
    println(newDirs.values.filter { it.size >= neededSpace }.minByOrNull { it.size }!!.size)
}

