
object Model {

  var accountList: List[Account] = List[Account]()

  def addAccount(name: String, amount: Double = 0): Account = {
    val id = java.util.UUID.randomUUID().toString
    val account = Account(id, name, amount)
    accountList ::= account
    account
  }

  def getAccount(id: String): Option[Account] = {
    accountList.find(a => a.id == id)
  }

  def tranfer(t: Transaction): Option[Unit] = {
    for {
      from <- getAccount(t.from)
      to <- getAccount(t.to)
      if t.amount <= from.amount
    } yield {
      val nFrom = from.copy(amount = from.amount - t.amount)
      val nTo = to.copy(amount = to.amount + t.amount)
      accountList.filter(a => a.id == t.from || a.id == t.to)
      accountList ::= nFrom
      accountList ::= nTo
    }
  }
}