import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Route
import spray.json._



trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val clientFormat: RootJsonFormat[Client] = jsonFormat2(Client)
  implicit val orderFormat: RootJsonFormat[Account] = jsonFormat3(Account)
  implicit val errorFormat: RootJsonFormat[Error] = jsonFormat1(Error)
  implicit val transferFormat: RootJsonFormat[Transfer] = jsonFormat3(Transfer)
}

trait RouteHandler extends JsonSupport {
  implicit val system: ActorSystem
  implicit val materializer: ActorMaterializer


  val route: Route =
    path("hello") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
      }
    } ~
      path("create") {
        post {
          entity(as[Client]) { client =>
            val account = Service.addAccount(client.name, client.amount)
            complete(account)
          }
        }
      } ~ path("transfer") {
      post {
        entity(as[Transfer]) { t =>
          val transfer: Either[Error, Transfer] = Service.transfer(t)
          complete(transfer)
        }
      }
    }
}