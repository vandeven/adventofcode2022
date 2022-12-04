import java.io.File
import java.nio.file.Files

class Main

fun String.readFile() = Main::class.java.getResource(this).readText().lines()