
import scala.collection.mutable.ArrayBuffer

  object BookConvert {
  implicit def bookConvTuple(t :(String, String)) = Book(t._1,t._2,2018)
 // implicit def bookConv(b:String*) = Book(b(0),b(1),22)
  implicit def bookExistingConv(tit:String)(implicit lib:Library)
               = Book(tit, lib.findPredicate(b=>b.title == tit).map(b=>b.author).lift(0).getOrElse("author not found"),2018)
}
import BookConvert._
object LibraryApp{
 implicit val lib: Library =Library()
  lib.addBook(Tuple2[String,String]("s","A")) 
  lib.addBook(Book("ta","as",23))
  lib.addBook(Book("tar","as",23))
  lib.addBook("tar","as")
  lib.addBook("tar") 
  def print  = lib.printBooks();
  def find(pred: Book => Boolean)= lib.findPredicate(pred)
  def get(pred: Book => Boolean)= lib.findPredicate(pred)
  def add(b:Book) = lib.addBook(b)
  def add(title:String,author:String) = lib.addBook(title,author);
  def add(b:String) = lib.addBook(b)
  def lend(t: String) = lib.deleteBook(t);

  
}

class Library(private var books: ArrayBuffer[Book] = new ArrayBuffer()){ 
 def printBooks(books: ArrayBuffer[Book] = this.books )
                = println(books.map(b=>s"Book: ${b.title}  ${b.author}  ${b.date}").mkString("\n"))
 def byAuthorFind(author:String): ArrayBuffer[Book] = this.books.filter(b=>b.author==author)
 def findPredicate(pred: Book => Boolean): ArrayBuffer[Book] = this.books.filter(pred)
 def getPredicate(pred: Book => Boolean) = this.books.find(pred)
 def addBook(b:Book)= this.books+=b
 def deleteBook(t :String) = this.books = this.books.filter(b=>b.title != t)      
}

object  Library{
 var book: ArrayBuffer[Book] = new ArrayBuffer()
   def deleteBook(t :String) = book = book.filter(b=>b.title != t)  
  def apply(books: ArrayBuffer[Book] = new ArrayBuffer()):
  Library = new Library(books)
}

case class Book(
  title: String,
  author:String,
  date : Int 
             )
