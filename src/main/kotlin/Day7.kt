sealed interface Command
data class CD(val name: String) : Command
data class LS(val values: List<Item>) : Command

sealed interface Item
data class File(val name: String, val size: Int) : Item
data class Directory(
    val name: String,
    var parent: String?,
    var directories: Set<String>,
    var files: Set<File>,
    var size: Int = 0,
    var walked : Boolean = false
) : Item {
    override fun equals(other: Any?): Boolean {
        return if (other is Directory) {
            other.name.equals(name)
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}


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
                            Directory(it.substring(4), null, setOf(), setOf())
                        } else {
                            File(it.substringAfter(" "), it.substringBefore(" ").toInt())
                        }
                    }
                )
            }
        }
    val allDirs = setOf(Directory("/", "", setOf(), setOf())).associateBy { it.name }.toMutableMap()

    commands.drop(1)
        .fold(allDirs["/"]!!) { currentDir, e ->
            when (e) {
                is CD -> when (e.name) {
                    ".." -> allDirs[currentDir.parent]!!
                    else -> {
                        allDirs["${currentDir.name}/${e.name}"]!!
                    }
                }
                is LS -> {
                    val directories = e.values.filterIsInstance<Directory>().map {"${currentDir.name}/${it.name}" }.toSet()
                    val files = e.values.filterIsInstance<File>().toSet()
                    currentDir.files = files
                    currentDir.directories = directories
                    directories.forEach {
                        allDirs.computeIfAbsent(it){ k -> Directory(k, currentDir.name, mutableSetOf(), mutableSetOf())}
                     }
                    currentDir
                }
            }
        }


    fun calculateSize(directory: Directory): Int {
        val dirSize = directory.directories.map { allDirs[it]!! }.sumOf { calculateSize(it) }
        val filesSize = directory.files.map { it.size }.sum()
        directory.walked = true
        directory.size = dirSize + filesSize
        return dirSize + filesSize
    }

    calculateSize(allDirs["/"]!!)

    println(allDirs.values.filter { it.size < 100000 }.map { it.size }.sum())

    val neededSpace = 30000000 - (70000000 - allDirs["/"]!!.size)
    println(allDirs.values.filter { it.size >= neededSpace }.minByOrNull { it.size }!!.size)
}

