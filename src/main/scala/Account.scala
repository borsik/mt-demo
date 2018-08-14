case class Account(id: String, name: String, amount: Double)

case class Client(name: String, amount: Double)

case class Transfer(from: String, to: String, amount: Double)

case class Error(message: String)
