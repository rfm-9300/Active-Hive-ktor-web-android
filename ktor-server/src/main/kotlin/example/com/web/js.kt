package example.com.web

import kotlinx.html.HtmlBlockTag
import kotlinx.html.script
import java.io.File

fun HtmlBlockTag.loadJs(folderName: String = "") {
    val directory = "files/js/$folderName"

    val RESET = "\u001B[0m"
    val RED = "\u001B[31m"
    val GREEN = "\u001B[32m"
    val YELLOW = "\u001B[33m"
    val BLUE = "\u001B[34m"
    val PURPLE = "\u001B[35m"
    val CYAN = "\u001B[36m"
    // load all js files in the directory

    // get all files in the directory
    val files = File(directory).listFiles()?.toList()?.filter { it.name.endsWith(".js") } ?: emptyList()
    val jsFileNames = files.map { it.name }

    println("$GREEN$jsFileNames$RESET")

    // return script tags for each file
    jsFileNames.forEach { fileName ->
        script(src = "/resources/js/$folderName/$fileName") {}
    }
}