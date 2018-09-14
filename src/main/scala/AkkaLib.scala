import akka.actor._
import akka.pattern._
import akka.util.Timeout
import akka.dispatch.ExecutionContexts._

import scala.collection.mutable._
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.concurrent.duration._

object  AkkaLib extends App {
  
  def workers_amount = 5
  def initWorkers(system: ActorSystem) :List[ActorRef] = (1 to workers_amount).toList.map(a=>system.actorOf(Props(new Worker()))) 
      
  override def main(args: Array[String]): Unit ={
    implicit val timeout: Timeout = Timeout(25 seconds)
    implicit val ec: ExecutionContextExecutor = global
    val system = ActorSystem("System")
    val actor = system.actorOf(Props(new Librarian(initWorkers(system))))
    val tasks = List((LibFunc.Get,"Dziady"),(LibFunc.Get,"Tytul"),(LibFunc.Add,"Dziady"),(LibFunc.Lend, "Tytul"))
     val future:Future[Any] = actor ? tasks
    future.map(res=> {
      res match{
        case res:Array[Any]=>println(res.mkString("\n"))
        case res => println(res)
      }  
      LibraryApp.print
      system.terminate
    })
   
  }
  
  class Librarian(workers : List[ActorRef]) extends Actor{ 
    val orders :HashMap[ActorRef,Array[Any]] = new HashMap()
    def receive = {
      case tasks: List[Any] => {
        for (i <- 0 until tasks.size)
          workers(i % AkkaLib.workers_amount) ! (sender(),tasks(i))
        orders += (sender() -> new Array[Any](tasks.size)) 
      }
      case (Response(client:ActorRef),res:Any)=>{
       val index = orders(client).indexOf(null)
       orders(client)(index) = res
       if(index==orders(client).size-1) 
         client ! orders(client)
        this.receive
      }
      case _ => sender ! "ERROR"
    }
  }
    case class Response(client : ActorRef)
    class Worker extends Actor{
      override def receive: Receive = {
        case (client :ActorRef,(LibFunc.Add,bookToAdd:String))=>sender ! (Response(client),LibraryApp.add(bookToAdd))
        case (client :ActorRef,(LibFunc.Get,title:String))=> sender ! (Response(client),LibraryApp.get(b=>b.title == title)) 
        case (client :ActorRef,(LibFunc.Lend,title:String))=> sender ! (Response(client),LibraryApp.lend(title))
        case (client :ActorRef,(LibFunc.Find,pred:(Book => Boolean)))=>sender ! (Response(client),LibraryApp.find(pred))
        case m => println("Error>>   " + m)
      }
    }  
  
  

}
