import org.scalatest._

class AccountSpec extends FlatSpec with Matchers {

  it should "add account" in {
    val account = Service.addAccount("John Doe", 200).right.get
    assert(account.name == "John Doe" && account.amount == 200)
  }

  it should "get account" in {
    val account = Service.addAccount("Jane Doe", 300).right.get
    val nAcc = Service.getAccount(account.id)
    assert(nAcc.exists(a => a.name == "Jane Doe" && a.amount == 300))
  }

  it should "make transaction" in {
    val from = Service.addAccount("Mike Doe", 400).right.get
    val to = Service.addAccount("Erica Doe", 10).right.get
    val transaction = Transfer(from.id, to.id, 100)
    Service.transfer(transaction)
    val nFrom = Service.getAccount(from.id).right.get
    val nTo = Service.getAccount(to.id).right.get
    assert(nFrom.amount == 300 && nTo.amount == 110)
  }
}
