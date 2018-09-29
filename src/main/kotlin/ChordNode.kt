class ChordNode(
        val m: Int,
        val id: Int
) {

    var successor = this

    var predecessor = this

    private val fingers: MutableList<Finger> = ArrayList(m)

    fun findSuccessor(id: Int) = findPredecessor(id).successor

    private fun findPredecessor(id: Int): ChordNode {
        var n0 = this
        while (id !in n0.id + 1..n0.successor.id)
            n0 = closestPrecedingNode(id)
        return n0
    }

    private fun closestPrecedingNode(id: Int): ChordNode = (fingers.size - 1 downTo 0)
            .firstOrNull { fingers[it].node.id in this.id + 1 until id }
            ?.let { fingers[it].node }
            ?: this

    fun join(n0: ChordNode? = null) = if (n0 != null) {
        initFingerTable(n0)
        updateOthers()
    } else {
        for (i in 0 until fingers.size) {
            fingers[i] = Finger(start(i), this)
        }
        predecessor = this
    }

    private fun initFingerTable(n0: ChordNode) {
        fingers[0] = Finger(start(0), n0.findSuccessor(start(0)))
        predecessor = successor.predecessor
        successor.predecessor = this
        for (i in 1 until m) {
            if (start(i + 1) in id until fingers[i].node.id)
                fingers[i + 1] = Finger(start(i), fingers[i].node)
            else
                fingers[i + 1] = Finger(start(i), n0.findSuccessor(start(i + 1)))
        }
    }

    private fun updateOthers() = fingers.forEachIndexed { i, finger ->
        findPredecessor(id - Math.pow(2.0, (i - 1).toDouble()).toInt())
                .updateFingerTable(this, i)
    }

    private fun updateFingerTable(s: ChordNode, i: Int) {
        if (s.id in id until fingers[i].node.id) {
            fingers[i] = Finger(start(i), s)
            predecessor.updateFingerTable(s, i)
        }
    }

    private fun start(i: Int) = (id + Math.pow(2.0, i.toDouble())).toInt() % m

}