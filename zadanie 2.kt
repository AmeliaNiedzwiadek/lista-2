/**
 * Abstrakcyjna klasa bazowa dla sekwencji biologicznych (DNA, RNA, Białko).
 * Klasa ta jest rozszerzana przez klasy specyficzne dla różnych typów sekwencji.
 * @param identifier Nagłówek FASTA, identyfikator sekwencji.
 * @param data Ciąg znaków reprezentujących sekwencję (np. "ATGC").
 * @param validChars Zbiór dozwolonych znaków w sekwencji, np. dla DNA: 'A', 'T', 'G', 'C'.
 * @throws IllegalArgumentException Jeśli sekwencja zawiera nieprawidłowe znaki, zgłaszany jest wyjątek.
 * @constructor Inicjalizuje obiekt z identyfikatorem, danymi sekwencji i dozwolonymi znakami.
 */
abstract class BioSequence(
    val identifier: String,
    var data: String,
    val validChars: Set<Char>
) {

    val length: Int
        get() = data.length

    init {

        require(data.all { it in validChars }) { "Nieprawidłowe znaki w sekwencji: $data" }
    }

    override fun toString(): String {
        return ">$identifier\n$data"
    }

    fun mutate(position: Int, value: Char) {
        require(position in data.indices) { "Pozycja poza zakresem." }
        require(value in validChars) { "Znak $value nie jest dozwolony." }
        data = data.substring(0, position) + value + data.substring(position + 1)
    }

    fun findMotif(motif: String): Int {
        return data.indexOf(motif)
    }
}


//Klasa reprezentująca sekwencję DNA.
//Dziedziczy po klasie BioSequence, definiując dozwolone znaki ('A', 'T', 'G', 'C').

class DNASequence(identifier: String, data: String) :
    BioSequence(identifier, data, setOf('A', 'T', 'G', 'C')) {


    //Tworzy komplementarną sekwencję DNA (A ↔ T, G ↔ C).

    fun complement(): String {
        return data.map {
            when (it) {
                'A' -> 'T'
                'T' -> 'A'
                'G' -> 'C'
                'C' -> 'G'
                else -> it
            }
        }.joinToString("")
    }


     //Transkrybuje sekwencję DNA do RNA (zamienia 'T' na 'U').

    fun transcribe(): RNASequence {
        val rnaData = data.replace('T', 'U')
        return RNASequence(identifier, rnaData)
    }
}


 // Klasa reprezentująca sekwencję RNA.
 //Dziedziczy po klasie BioSequence, definiując dozwolone znaki ('A', 'U', 'G', 'C').

class RNASequence(identifier: String, data: String) :
    BioSequence(identifier, data, setOf('A', 'U', 'G', 'C')) {


     //Tworzy komplementarną sekwencję RNA (A ↔ U, G ↔ C).

    fun complement(): String {
        return data.map {
            when (it) {
                'A' -> 'U'
                'U' -> 'A'
                'G' -> 'C'
                'C' -> 'G'
                else -> it
            }
        }.joinToString("")
    }


     //Transkrybuje sekwencję RNA do białka (zastępuje kodony RNA na jedno-literowe reprezentacje aminokwasów).
     //Uproszczona wersja translacji.

    fun transcribe(): ProteinSequence {
        val proteinData = data.chunked(3).map { "A" }.joinToString("") // Zastąpiono kodonem 'A'
        return ProteinSequence(identifier, proteinData)
    }
}


 //Klasa reprezentująca sekwencję białka.
 //Dziedziczy po klasie BioSequence, definiując dozwolone litery aminokwasów ('A'..'Z' minus kilka wyjątków).

class ProteinSequence(identifier: String, data: String) :
    BioSequence(identifier, data, ('A'..'Z').toSet() - setOf('B', 'J', 'O', 'U', 'Z')) {
    // Wykluczamy tylko nieistniejące aminokwasy: B, J, O, U, Z
}




// Funkcja pomocnicza do testów
fun assertEquals(expected: Any, actual: Any, testName: String) {
    val result = if (expected == actual) "OK" else "FAIL — Expected: $expected, Actual: $actual"
    println("$testName: $result")
}

//Funkcja testowa, która sprawdza poprawność działania klas i metod.

fun main() {
    val dna = DNASequence("DNA1", "ATGC")

    // Test podstawowy
    testBasicDNASequence(dna)

    // Testowanie mutacji z różnymi scenariuszami
    testMutations(dna)

    // Testowanie motywów
    testMotifs(dna)

    // Testowanie transkrypcji i komplementów
    testTranscriptionAndComplement(dna)
}

fun testBasicDNASequence(dna: DNASequence) {
    println("\n=== Test podstawowy ===")
    println(dna)
    println("Długość: ${dna.length}")
}

fun testMutations(dna: DNASequence) {
    println("\n=== Testowanie mutacji ===")

    // Mutacja na poprawnej pozycji
    dna.mutate(2, 'T')
    println("Po mutacji na pozycji 2: $dna")
    assertEquals("ATTC", dna.data, "Mutacja na pozycji 2")

    // Mutacja poza zakresem
    try {
        dna.mutate(10, 'A')
    } catch (e: IllegalArgumentException) {
        println("Błąd przy mutacji poza zakresem: ${e.message}")
    }
    assertEquals("ATTC", dna.data, "Mutacja poza zakresem")

    // Mutacja z niedozwolonym znakiem
    try {
        dna.mutate(0, 'X')
    } catch (e: IllegalArgumentException) {
        println("Błąd przy mutacji z niedozwolonym znakiem: ${e.message}")
    }
    assertEquals("ATTC", dna.data, "Mutacja z niedozwolonym znakiem")
}

fun testMotifs(dna: DNASequence) {
    println("\n=== Testowanie motywów ===")

    // Szukanie motywu
    val motifPos = dna.findMotif("TT")
    println("Motyw 'TT' na pozycji: $motifPos")
    assertEquals(1, motifPos, "Znaleziony motyw 'TT'")

    // Brak motywu
    val noMotifPos = dna.findMotif("GGG")
    println("Motyw 'GGG' na pozycji: $noMotifPos")
    assertEquals(-1, noMotifPos, "Brak motywu 'GGG'")
}

fun testTranscriptionAndComplement(dna: DNASequence) {
    println("\n=== Testowanie transkrypcji i komplementów ===")

    // Test komplementarnej nici DNA
    val complement = dna.complement()
    println("Komplementarna sekwencja DNA: $complement")
    assertEquals("TAAG", complement, "Komplementarna sekwencja DNA")

    // Test transkrypcji DNA do RNA
    val rna = dna.transcribe()
    println("Transkrypcja DNA do RNA: $rna")
    assertEquals("AUUC", rna.data, "Transkrypcja DNA na RNA")

    // Test komplementarnego RNA
    val rnaComplement = rna.complement()
    println("Komplementarne RNA: $rnaComplement")
    assertEquals("UAAG", rnaComplement, "Komplementarne RNA")
}


//Żródła wykorzystane do napisania kodu:
//kotlinlang.org/docs/inheritance.html
//kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/index.html
//kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/
//kotlinlang.org/api/latest/jvm/stdlib/kotlin/-require/
