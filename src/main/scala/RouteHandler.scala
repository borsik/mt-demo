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
  implicit val requestFormat: RootJsonFormat[Request] = jsonFormat1(Request)
}

trait RouteHandler extends JsonSupport {
  implicit val system: ActorSystem
  implicit val materializer: ActorMaterializer


  val route: Route =
      path("add") {
        post {
          entity(as[Client]) { client =>
            val account = Service.addAccount(client.name, client.amount)
            val statusCode = account match {
              case Left(_) => StatusCodes.BadRequest
              case Right(_) => StatusCodes.OK
            }
            complete(statusCode, account)
          }
        }
      } ~ path("transfer") {
      post {
        entity(as[Transfer]) { t =>
          val transfer: Either[Error, Transfer] = Service.transfer(t)
          val statusCode = transfer match {
            case Left(_) => StatusCodes.BadRequest
            case Right(_) => StatusCodes.OK
          }
          complete(statusCode, transfer)
        }
      }
    } ~ path("get") {
        post {
          entity(as[Request]) { request =>
            val account = Service.getAccount(request.id)
            val statusCode = account match {
              case Left(_) => StatusCodes.NotFound
              case Right(_) => StatusCodes.OK
            }
            complete(statusCode, account)
          }
        }
      }
}