
import scala.collection.mutable.ArrayBuffer

  object BookConvert {
    val r = new scala.util.Random

  implicit def bookConvTuple(t :(String, String)) = Book(t._1,t._2,r.nextInt(30))
 // implicit def bookConv(b:String*) = Book(b(0),b(1),22)
  implicit def bookExistingConv(tit:String)(implicit lib:Library)
               = Book(tit, lib.findPredicate(b=>b.title == tit).map(b=>b.author).lift(0).getOrElse("author not found"),r.nextInt(30))
}
import BookConvert._
object LibraryApp{
 implicit val lib: Library =Library()
  lib.addBook(Tuple2[String,String]("Dziady","Mickiewicz")) 
  lib.addBook(Book("Lalka","Prus",23))
  lib.addBook(Book("Paragraf 22","nwm",30))
  lib.addBook("Tytul","Pisarz")
  lib.addBook("Tytul","Pisarz") 
  def print  = lib.printBooks();
  def find(pred: Book => Boolean)= lib.findPredicate(pred)
  def get(pred: Book => Boolean)= lib.getPredicate(pred)
  def add(b:Book) = lib.addBook(b)
  def add(title:String,author:String) = lib.addBook(title,author);
  def add(b:String) = lib.addBook(b)
  def lend(t: String) = lib.deleteBook(t);
}
object LibFunc extends Enumeration{
  type  LibFunc = Value
  val Lend,Add,Find,Print,Get = Value 
}

class Library(private var books: ArrayBuffer[Book] = new ArrayBuffer()){ 
 def printBooks(books: ArrayBuffer[Book] = this.books )
                = println(books.map(b=>s"Book: ${b.title}  ${b.author}  ${b.isbn}").mkString("\n"))
 def byAuthorFind(author:String): ArrayBuffer[Book] = this.books.filter(b=>b.author==author)
 def findPredicate(pred: Book => Boolean): ArrayBuffer[Book] = this.books.filter(pred)
 def getPredicate(pred: Book => Boolean) = this.books.find(pred)
 def addBook(b:Book)= this.books+=b
 def deleteBook(t :String):String ={ this.books = this.books.filter(b=>b.title != t); "deleted" }     
}

object  Library{  
    def apply(books: ArrayBuffer[Book] = new ArrayBuffer()):
    Library = new Library(books)
}

case class Book(
                 title: String,
                 author:String,
                 isbn : Int 
             )
