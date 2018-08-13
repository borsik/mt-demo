import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest._

class RouteSpec extends FlatSpec with Matchers with ScalatestRouteTest with RouteHandler {

  "Get request" should "return Say hello" in {
    Get("/hello") ~> route ~> check {
      responseAs[String] shouldEqual "<h1>Say hello to akka-http</h1>"
    }
  }


}
