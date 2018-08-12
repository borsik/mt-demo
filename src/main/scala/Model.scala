
object Model {

  var accountList: List[Account] = List[Account]()

  def addAccount(name: String, amount: Double = 0): Account = {
    val id = java.util.UUID.randomUUID().toString
    val account = Account(id, name, amount)
    accountList ::= account
    account
  }

  def getAccount(id: String): Either[Error, Account] = {
    accountList.find(a => a.id == id) match {
      case Some(account) => Right(account)
      case None => Left(Error(s"Account $id not found"))
    }
  }

  def isEnough(account: Account, amount: Double): Either[Error, Boolean] = {
    if (account.amount <= amount)
      Right(true)
    else
      Left(Error(s"Not enough funds"))
  }

  def tranfer(t: Transaction): Either[Error, Success] = {
    for {
      from <- getAccount(t.from)
      to <- getAccount(t.to)
      enough <- isEnough(from, t.amount)
      if enough
    } yield {
      val nFrom = from.copy(amount = from.amount - t.amount)
      val nTo = to.copy(amount = to.amount + t.amount)
      accountList.filter(a => a.id == t.from || a.id == t.to)
      accountList ::= nFrom
      accountList ::= nTo
      Success(s"Transaction from $nFrom to $nTo succeed")
    }
  }


}