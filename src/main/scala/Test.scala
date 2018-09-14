
object Test extends App { 
implicit  val imp= "Inject" 
   println(new Array(3).mkString(" ").toString)
  
  def printVal[T]()(implicit imp : T) {
    var list = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 0, -12)
    val t = List(Tuple2("nice", 3), Tuple2("try", 34), Tuple2("seven", 7))
    case class N(word: String, num: Int)
    val exi = for {
      i <- list
      tw <- t.find(s => s._2 - 30 == i)
      if tw != None
    } yield N(tw._1, i)
    imp match {
      case im: String => println (s"Scala ${imp} ${exi.map(n => n.num + ":" + n.word).mkString(">")} ")
      case _ =>  println (s"executionStart = ${executionStart}")
    }
   
  }
}



