const val M_intM = 4
val m_arPos = listOf(3, 8, 15, 30)

fun main(args: Array<String>) {
    println("Hello, this is Chord Protocol!\n")
    val chord = mutableListOf<ChordNode>()
    m_arPos.forEach {
        val chordNode = ChordNode(M_intM, it)
        chordNode.join(chord.firstOrNull())
        chord.add(chordNode)
    }
    printState(chord)
}

fun printState(chord: List<ChordNode>) {
    println("M_intM = $M_intM")
    println("Узлы:\n")
    chord.forEachIndexed { ind, it ->
        println("Узел $ind: id = ${it.id}")
        println("Finger table:")
        it.fingers.forEach {
            it?.let {
                println("start = ${it.start} interval = [${it.interval.first}, ${it.interval.second})")
            } ?: println("null")
        }
        println()
    }
}