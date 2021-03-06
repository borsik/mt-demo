import java.util.concurrent.atomic.AtomicReference

object Service {

  private val accounts = new AtomicReference(Map[String, Account]())

  def addAccount(name: String, amount: Double = 0): Either[Error, Account] = {
    if (amount < 0) {
      Left(Error("Amount can't be negative"))
    } else {
      val id = java.util.UUID.randomUUID().toString
      val account = Account(id, name, amount)
      accounts.set(accounts.get() + (id -> account))
      Right(account)
    }
  }

  def getAccount(id: String): Either[Error, Account] = {
    accounts.get().get(id) match {
      case Some(account) => Right(account)
      case None => Left(Error(s"Account $id not found"))
    }
  }

  private def add(account: Account, amount: Double): Either[Error, Account] = {
    if (amount < 0)
      Left(Error("Transaction amount can't be negative"))
    else
      Right(account.copy(amount = account.amount + amount))
  }

  private def diff(account: Account, amount: Double): Either[Error, Account] = {
    if (amount < 0 || account.amount < amount)
      Left(Error("Transaction can't be negative or more than account balance"))
    else
      Right(account.copy(amount = account.amount - amount))
  }


  def transfer(t: Transfer): Either[Error, Transfer] = {
    for {
      from <- getAccount(t.from)
      to <- getAccount(t.to)
      nFrom <- diff(from, t.amount)
      nTo <- add(to, t.amount)
    } yield {
      accounts.set(accounts.get() + (nFrom.id -> nFrom, nTo.id -> nTo))
      t
    }
  }


}