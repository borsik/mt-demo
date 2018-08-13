import akka.http.scaladsl.model.{HttpEntity, HttpMethods, HttpRequest, MediaTypes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.util.ByteString
import org.scalatest._

class RouteSpec extends FlatSpec with Matchers with ScalatestRouteTest with RouteHandler {

  "Get request" should "return Say hello" in {
    Get("/hello") ~> route ~> check {
      responseAs[String] shouldEqual "<h1>Say hello to akka-http</h1>"
    }
  }

  it should "return Account" in {
    val json = """{
                 |	"name": "John Doe",
                 |	"amount": 200
                 |}""".stripMargin

    val postRequest = HttpRequest(
      HttpMethods.POST,
      uri = "/create",
      entity = HttpEntity(MediaTypes.`application/json`, ByteString(json)))

    postRequest ~> route ~> check {
      val account = responseAs[Account]
      account.name shouldEqual "John Doe"
      account.amount shouldEqual 200
    }
  }


}
