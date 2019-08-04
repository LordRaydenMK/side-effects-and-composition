package coffee

data class Coffee(val price: Long = 3)

data class CreditCard(val no: String) {

    fun charge(price: Long): Unit = TODO()
}

class Payments {

    fun charge(cc: CreditCard, price: Long): Unit = TODO()
}

data class Charge(val cc: CreditCard, val price: Long)