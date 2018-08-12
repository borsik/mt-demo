import org.scalatest._

class AccountSpec extends FlatSpec with Matchers {
  "AccountList" should "have 0 size on init" in {
    assert(Model.accountList.isEmpty)
  }

  it should "add account" in {
    val account = Model.addAccount("John Doe", 200)
    assert(account.name == "John Doe" && account.amount == 200)
    assert(Model.accountList.size == 1)
  }

  it should "get account" in {
    val account = Model.addAccount("Jane Doe", 300)
    val nAcc = Model.getAccount(account.id)
    assert(nAcc.exists(a => a.name == "Jane Doe" && a.amount == 300))
    assert(Model.accountList.size == 2)
  }

  it should "transfer" in {
    val from = Model.addAccount("Mike Doe", 400)
    val to = Model.addAccount("Erica Doe", 10)
    val transaction = Transaction(from.id, to.id, 100)
    Model.tranfer(transaction)
    val nFrom = Model.getAccount(from.id).right.get
    val nTo = Model.getAccount(to.id).right.get
    assert(nFrom.amount == 300 && nTo.amount == 110)
  }
}
