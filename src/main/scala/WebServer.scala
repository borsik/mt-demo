import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val itemFormat: RootJsonFormat[Client] = jsonFormat2(Client)
  implicit val orderFormat: RootJsonFormat[Account] = jsonFormat3(Account)
  implicit val errorFormat: RootJsonFormat[Error] = jsonFormat1(Error)
  implicit val successFormat: RootJsonFormat[Success] = jsonFormat1(Success)
  implicit val transactionFormat: RootJsonFormat[Transaction] = jsonFormat3(Transaction)
}

object WebServer extends JsonSupport {
  def main(args: Array[String]) {

    implicit val system: ActorSystem = ActorSystem("my-system")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    val route =
      path("hello") {
        get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
        }
      } ~
      path("create") {
        post {
          entity(as[Client]) { client =>
            val account = Model.addAccount(client.name, client.amount)
            complete(account)

          }
        }
      } ~ path("transfer") {
        post {
          entity(as[Transaction]) { t =>
            val message: Either[Error, Success] = Model.transfer(t)
            complete(message)
          }
        }
      }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
