package coffee

data class Coffee(val price: Long)

data class CreditCard(val no: String) {

    fun charge(cup: Coffee): Unit = TODO()
}

class Payment {

    fun charge(cc: CreditCard, price: Long): Unit = TODO()
}