/**
 * Klasa reprezentuje prosty wielomian jednej zmiennej.
 * Współczynniki są zapisane w liście od x^0 do x^n.
 * Przykład: listOf(1.0, 2.0, 3.0) oznacza wielomian 1 + 2x + 3x²
 * @param wspolczynniki Lista współczynników.
 * @throws IllegalArgumentException Gdy lista jest pusta.
 */
class Wielomian(val wspolczynniki: MutableList<Double>) {

    init {
        if (wspolczynniki.isEmpty()) {
            throw IllegalArgumentException("Wielomian nie może być pusty.")
        }
        while (wspolczynniki.size > 1 && wspolczynniki.last() == 0.0) {
            wspolczynniki.removeLast()
        }
    }


     // Zwraca stopień wielomianu.

    fun stopien(): Int = wspolczynniki.size - 1


     //Zwraca tekstową reprezentację wielomianu.

    override fun toString(): String {
        var tekst = "W(x) = "
        for (i in wspolczynniki.size - 1 downTo 0) {
            tekst += "${wspolczynniki[i]}*x^$i"
            if (i > 0) tekst += " + "
        }
        return tekst
    }


    // Oblicza wartość wielomianu dla danej liczby x.

    operator fun invoke(x: Double): Double {
        var suma = 0.0
        for (i in wspolczynniki.indices) {
            suma += wspolczynniki[i] * Math.pow(x, i.toDouble())
        }
        return suma
    }


     //Dodaje dwa wielomiany.

    operator fun plus(other: Wielomian): Wielomian {
        val max = maxOf(this.wspolczynniki.size, other.wspolczynniki.size)
        val nowa = MutableList(max) { 0.0 }
        for (i in 0 until max) {
            val a = this.wspolczynniki.getOrElse(i) { 0.0 }
            val b = other.wspolczynniki.getOrElse(i) { 0.0 }
            nowa[i] = a + b
        }
        return Wielomian(nowa)
    }


     //Odejmuje dwa wielomiany

    operator fun minus(other: Wielomian): Wielomian {
        val max = maxOf(this.wspolczynniki.size, other.wspolczynniki.size)
        val nowa = MutableList(max) { 0.0 }
        for (i in 0 until max) {
            val a = this.wspolczynniki.getOrElse(i) { 0.0 }
            val b = other.wspolczynniki.getOrElse(i) { 0.0 }
            nowa[i] = a - b
        }
        return Wielomian(nowa)
    }


     //Mnoży dwa wielomiany.

    operator fun times(other: Wielomian): Wielomian {
        val nowa = MutableList(this.stopien() + other.stopien() + 1) { 0.0 }
        for (i in this.wspolczynniki.indices) {
            for (j in other.wspolczynniki.indices) {
                nowa[i + j] += this.wspolczynniki[i] * other.wspolczynniki[j]
            }
        }
        return Wielomian(nowa)
    }
}
fun testWielomian() {
    println("TESTY KLASY WIELOMIAN")

    // Przykładowe wielomiany
    val w1 = Wielomian(mutableListOf(1.0, 2.0, 3.0)) // 3x^2 + 2x + 1
    val w2 = Wielomian(mutableListOf(0.0, -1.0, 1.0)) // x^2 - x

    println("Reprezentacja w1: ${w1}")
    println("Reprezentacja w2: ${w2}")
    println("Stopień w1: ${w1.stopien()}")
    println("Stopień w2: ${w2.stopien()}")
    println("Wartość w1(2.0): ${w1(2.0)}")
    println("Wartość w2(2.0): ${w2(2.0)}")

    val suma = w1 + w2
    println("Wynik dodawania: $suma")

    val roznica = w1 - w2
    println("Wynik odejmowania: $roznica")

    val iloczyn = w1 * w2
    println("Wynik mnożenia: $iloczyn")

    // Wielomian zerowy
    val zerowy = Wielomian(mutableListOf(0.0))
    println("Wielomian zerowy: $zerowy")

    // Jednowyrazowy (stały)
    val staly = Wielomian(mutableListOf(7.0))
    println("Wielomian stały: $staly")

    // Duży stopień (x^10)
    val dziesiaty = Wielomian(MutableList(11) { if (it == 10) 1.0 else 0.0 })
    println("Wielomian x^10: $dziesiaty")

    // Bardzo duży wielomian
    val duzy = Wielomian((1..100).map { it.toDouble() }.toMutableList())
    println("Wielomian 100-stopniowy: stopień = ${duzy.stopien()}")

    // Wartości brzegowe
    println("w1(0) = ${w1(0.0)}")
    println("w1(-1) = ${w1(-1.0)}")
    println("w1(1000000.0) = ${w1(1000000.0)}")

    // Błąd: pusta lista
    try {
        val blad = Wielomian(mutableListOf())
        println("BŁĄD – nie zgłoszono wyjątku dla pustej listy.")
    } catch (e: IllegalArgumentException) {
        println("OK – wyjątek przy pustej liście: ${e.message}")
    }

    // Zerowy z wielu zer
    val wielozerowy = Wielomian(mutableListOf(0.0, 0.0, 0.0))
    println("Zerowy z wielu zer: $wielozerowy")
    println("Stopień: ${wielozerowy.stopien()}")
    println("W(2) = ${wielozerowy(2.0)}")

    println("KONIEC TESTÓW")
}

fun main() {
    testWielomian()
}

//Kod został napisany za pomocą dokumentacji języka Kotlin:
//kotlinlang.org/api/latest/jvm/stdlib/kotlin.math/pow/
//kotlinlang.org/docs/classes.html
//kotlinlang.org/docs/control-flow.html
