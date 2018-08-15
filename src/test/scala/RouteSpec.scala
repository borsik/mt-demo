import akka.http.scaladsl.model.{HttpEntity, HttpMethods, HttpRequest, MediaTypes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.util.ByteString
import org.scalatest._

class RouteSpec extends FlatSpec with Matchers with ScalatestRouteTest with RouteHandler {


  def createRequest(uri: String, json: String): HttpRequest = {
    HttpRequest(
      HttpMethods.POST,
      uri = uri,
      entity = HttpEntity(MediaTypes.`application/json`, ByteString(json)))
  }


  it should "return Account" in {
    val json = """{
                 |	"name": "John Doe",
                 |	"amount": 200
                 |}""".stripMargin


    createRequest("/add", json) ~> route ~> check {
      val account = responseAs[Account]
      account.name shouldEqual "John Doe"
      account.amount shouldEqual 200
    }
  }

  it should "return can't be negative" in {
    val json = """{
                 |	"name": "Jane Doe",
                 |	"amount": -200
                 |}""".stripMargin

    createRequest("/add", json) ~> route ~> check {
      val error = responseAs[Error]
      error.message shouldEqual "Amount can't be negative"
    }
  }

  it should "make transaction" in {
    val erica = Service.addAccount("Erica", 200).right.get
    val nina = Service.addAccount("Nina", 100).right.get

    val json = s"""{
                  |	"from": "${erica.id}",
                  |	"to": "${nina.id}",
                  | "amount": 100
                  |}""".stripMargin

    createRequest("/transfer", json) ~> route ~> check {
      val transfer = responseAs[Transfer]
      transfer.from shouldEqual erica.id
      transfer.to shouldEqual nina.id
      transfer.amount shouldEqual 100
    }
  }

  it should "return client" in {
    val jackie = Service.addAccount("Jackie", 200).right.get

    val json = s"""{
                  |	"id": "${jackie.id}"
                  |}""".stripMargin

    createRequest("/get", json) ~> route ~> check {
      val account = responseAs[Account]
      account.id shouldEqual jackie.id
      account.name shouldEqual jackie.name
      account.amount shouldEqual jackie.amount
    }
  }

}
