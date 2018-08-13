import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn


class WebServer(implicit val system:ActorSystem,
                implicit  val materializer:ActorMaterializer) extends RouteHandler {

  implicit val ec: ExecutionContextExecutor = system.dispatcher

  def bind(address: String, port: Int): Unit = {
    val bindingFuture = Http().bindAndHandle(route, address, port)
    println(s"Server online at $address:$port/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}

object WebServer {

  def main(args: Array[String]) {
    implicit val system: ActorSystem = ActorSystem("mt-demo")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    val server = new WebServer()
    server.bind("localhost", 8080)
  }

}
