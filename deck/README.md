## Side Effects and Composition

### Stojan Anastasov

---

## Modeling a Coffee shop

- Buying coffee
- Card payment

---

## Buying coffee V1

```kotlin

import coffee.*

fun buyCoffee(cc: CreditCard): Coffee {
    val cup = Coffee()
    cc.charge(cup.price)
    return cup
}
```

---

## Testability

```kotlin
cc.charge(cup.price) // <-- Side effect
```

Talks to the CC company via API/SDK <!-- .element: class="fragment" data-fragment-index="1" -->

Should a CC know how it's charged <!-- .element: class="fragment" data-fragment-index="2" -->

---

## Buying Coffee V2

```kotlin

fun buyCoffee(cc: CreditCard, p: Payments): Coffee {
    val cup = Coffee()
    p.charge(cc, cup.price)
    return cup
}
```

DI to the rescue - mock `Payments` in tests

---

## New requirements

- Buy coffee
- Buy X coffees
- One charge per credit card

---

## Composition

Reuse the code for 1 coffee for X coffees

```kotlin

fun buyCoffees(
    cc: CreditCard,
    p: Payments,
    n: Int
): List<Coffee> = List(n) { buyCoffee(cc, p) }
```
<!-- .element: class="fragment" data-fragment-index="1" -->

Charges the same CC multiple times :( <!-- .element: class="fragment" data-fragment-index="2" -->

---

## Side effect

```kotlin
p.charge(cc, cup.price) // <-- Side effect
```

Charging the Credit Card prevents composition

---

## Buying Coffee V3

```kotlin

fun buyCoffee(cc: CreditCard): Pair<Coffee, Charge> {
    val cup = Coffee()
    val charge = Charge(cc, cup.price)
    return Pair(cup, charge)
}
```

Return a value (indicating the side effect) instead of performing a side effect

---

## Combine charges

```kotlin

fun combine(c1: Charge, c2: Charge): Charge =
    if (c1.cc == c2.cc) Charge(c1.cc, c1.price + c2.price)
    else throw IllegalArgumentException(
        "Can't combine charges with different cc"
    )
```

---

## Buying X Coffees

```kotlin

fun buyCoffees(
    cc: CreditCard,
    n: Int
): Pair<List<Coffee>, Charge> {
    val purchases: List<Pair<Coffee, Charge>> =
        List(n) { buyCoffee(cc) }
    val (coffees, charges) = purchases.unzip()
    val charge = charges.reduce { c1, c2 -> combine(c1, c2) }
    return Pair(coffees, charge)
}
```

Removing the side effect makes the function composable.

---

## Alternative solution

`BatchPaymentProcessor`

---

## Composition and Side effects

To achieve composition don't mix side effect with business logic.
