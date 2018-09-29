class ChordNode(
        m: Int,
        val successor: ChordNode,
        val predecessor: ChordNode
) {

    val fingers: List<Finger> = ArrayList(m)

}