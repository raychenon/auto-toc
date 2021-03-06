package readme

object TableOfContentHelper {

  val prefix = "#"

  def convert(readme: String): Seq[String] = {
    val lines: Seq[LineIndex] = tableOfContent(readme)
    if (lines.isEmpty) {
      Seq("The text has no meta title")
    }else {
      generateTableOfContent(lines)
    }
  }

  private def tableOfContent(readme: String): Seq[LineIndex] = {

    val lines = readme.split("\n")
    val topics = lines.map(_.stripMargin).filter(x => x.startsWith(prefix))

    for (line <- topics) yield (getLindex(line))
  }

  private def getLindex(line: String): LineIndex = {
    val count = line.count(_ == '#')
    val text = line.substring(count, line.length).trim
    val title = s"- [$text](#${text.toLowerCase.split(' ').mkString("-")})"
    LineIndex(count, title)
  }

  val SPACE: String = "&nbsp;"

  // non-breakable space
  private def generateTableOfContent(list: Seq[LineIndex]): Seq[String] = {
    val min = list.reduceLeft(minIndentation).indentation
    for (line <- list) yield (
      s"""${repeatChar(SPACE, line.indentation - min)} ${line.title}""")
  }

  private def repeatChar(c: String, n: Int): String = (for (i <- 1 to n) yield c).mkString

  private def printTableOfContent(list: Seq[LineIndex]): String = {
    val seq = generateTableOfContent(list)
    seq.mkString("\n")
  }

  private def minIndentation(l1: LineIndex, l2: LineIndex): LineIndex = if(l1.indentation < l2.indentation) l1 else l2

}
