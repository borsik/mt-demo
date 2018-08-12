case class Account(id: String, name: String, amount: Double)

case class Client(name: String, amount: Double)

case class Transaction(from: String, to: String, amount: Double)

case class Error(message: String)

case class Success(message: String)

