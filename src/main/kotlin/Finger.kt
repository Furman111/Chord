data class Finger(
        val start: Int,
        val interval: Pair<Int, Int>,
        val node: ChordNode
) {

    companion object {

        fun create(m: Int, id: Int, i: Int, node: ChordNode): Finger {
            val start = start(m, id, i)
            val secondStart = start(m, id, i + 1)
            return Finger(
                    start,
                    Pair(start, secondStart),
                    node
            )
        }

        fun start(m: Int, id: Int, i: Int) = (id + Math.pow(2.0, i.toDouble()).toInt()) % Math.pow(2.0, m.toDouble()).toInt()

    }

}