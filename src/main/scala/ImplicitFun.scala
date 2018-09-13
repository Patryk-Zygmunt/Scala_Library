class DonutString(s: String) {
  def isFavoriteDonut: Boolean = s == "Glazed Donut"

}

  object DonutConverstions {
  implicit def stringToDonutString(s: String) = new DonutString(s)
}



import DonutConverstions._
object ImplicitFun extends App{
 val ngl = "gg"  
 val gl = "Glazed Donut"  
println(s"${gl.isFavoriteDonut}")  
println(s"${ngl.isFavoriteDonut}")  
  
}
