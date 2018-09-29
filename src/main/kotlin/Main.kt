const val M_intM = 3
val m_arPos = listOf(0, 1, 3)

fun main(args: Array<String>) {
    println("Добрый день, это Chord Protocol!\n")
    val chord = mutableListOf<ChordNode>()
    m_arPos.forEach {
        val chordNode = ChordNode(M_intM, it)
        chordNode.join(chord.firstOrNull())
        chord.add(chordNode)
    }
    printState(chord)

    println("Добавляем узел с идентификатором 6\n")
    val chordNode = ChordNode(M_intM, 6)
    chordNode.join(chord.firstOrNull())
    chord.add(chordNode)

    printState(chord)

    println("Удаляем узел с идентификатором 6\n")
    chordNode.remove()
    chord.remove(chordNode)

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
                println("start = ${it.start} interval = [${it.interval.first}, ${it.interval.second}) successor = ${chord.firstOrNull()?.findSuccessor(it.start)?.id}")
            } ?: println("null")
        }
        println()
    }
}