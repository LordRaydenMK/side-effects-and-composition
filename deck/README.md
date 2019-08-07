## Side Effects and Composition

### Stojan Anastasov

---

<img src="https://images.manning.com/720/960/resize/book/2/a2ed920-d6ed-48fb-8f18-b051b7a09a2a/bjarnason.png" alt="FP in Scala - Manning" width="500px"/>

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

- Buy coffee <!-- .element: class="fragment fade-in-then-semi-out" data-fragment-index="0" -->
- Card payment <!-- .element: class="fragment fade-in-then-semi-out" data-fragment-index="0" -->
- Buy X coffees <!-- .element: class="fragment" data-fragment-index="1" -->
- One charge per credit card <!-- .element: class="fragment" data-fragment-index="2" -->

---

## Composition

Reuse the code for 1 coffee for X coffees

- It's complex (could be) <!-- .element: class="fragment" data-fragment-index="1" -->
- It's tested <!-- .element: class="fragment" data-fragment-index="2" -->
- It's debugged <!-- .element: class="fragment" data-fragment-index="3" -->
- Saves time <!-- .element: class="fragment" data-fragment-index="4" -->

---

## Buy multiple coffees

```kotlin

fun buyCoffees(
    cc: CreditCard,
    p: Payments,
    n: Int
): List<Coffee> = List(n) { buyCoffee(cc, p) }
```

Charges the same CC multiple times :( <!-- .element: class="fragment" data-fragment-index="1" -->

---

## Side effect

```kotlin
p.charge(cc, cup.price) // <-- Side effect
```

Charging the Credit Card prevents composition

---

## What is a side effect

Something that breaks referential transparency <!-- .element: class="fragment" data-fragment-index="1" -->

---

## Referential Transparency

> An expression is called referentially transparent if it can be replaced with its corresponding value without changing the program's behavior. - Wikipedia

---

## RT Example

```kotlin

fun buyCoffee(cc: CreditCard, p: Payments): Coffee {
    val cup = Coffee()
    p.charge(cc, cup.price)
    return cup
}
```

```kotlin
val coffeeA = buyCoffee(cc, p)

val coffeeB = Coffee()
```

coffeeA is not the same as coffeeB -> means buyCoffee() is not referentially transparent <!-- .element: class="fragment" data-fragment-index="1" -->

---

### BatchPaymentProcessor

A Payment processor that can batch requests for the same Card

- How long do we wait <!-- .element: class="fragment" data-fragment-index="1" -->
- How many charges do we batch <!-- .element: class="fragment" data-fragment-index="1" -->
- Does buyCoffee() indicate start/end of a batch<!-- .element: class="fragment" data-fragment-index="1" -->

Also code doesn't fit in a slide <!-- .element: class="fragment" data-fragment-index="2" -->

---

### Can we do better

---

## Buying Coffee V3

```kotlin

fun buyCoffee(cc: CreditCard): Pair<Coffee, Charge> {
    val cup = Coffee()
    val charge = Charge(cc, cup.price)
    return Pair(cup, charge)
}
```

Return a value (indicating the effect) instead of performing a side effect. We perform the side effect later.

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

---

## Benefits

- Better testability (no need for mocks) <!-- .element: class="fragment" data-fragment-index="1" -->
- Composition <!-- .element: class="fragment" data-fragment-index="2" -->
- Separation of concerns <!-- .element: class="fragment" data-fragment-index="3" -->

---

## Composition and Side effects

To achieve composition don't mix side effect with business logic.

---

Questions?
