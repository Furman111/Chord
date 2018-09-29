class ChordNode(
        val m: Int,
        val id: Int
) {

    private var successor = this

    private var predecessor = this

    val fingers: MutableList<Finger?> = MutableList(m) {
        null
    }

    fun findSuccessor(id: Int) = findPredecessor(id).successor

    private fun findPredecessor(id: Int): ChordNode {
        var n0 = this
        if (successor != this) {
            while (id !in n0.id + 1..n0.successor.id)
                n0 = n0.closestPrecedingNode(id)
        }
        return n0
    }

    private fun closestPrecedingNode(id: Int): ChordNode = (fingers.size - 1 downTo 0)
            .firstOrNull { fingers[it]?.node?.id in this.id + 1 until id }
            ?.let { fingers[it]?.node }
            ?: this

    fun join(n0: ChordNode? = null) = if (n0 != null) {
        initFingerTable(n0)
        updateOthers()
    } else {
        for (i in 0 until fingers.size) {
            fingers[i] = Finger.create(m, id, i, this)
        }
        predecessor = this
    }

    private fun initFingerTable(n0: ChordNode) {
        fingers[0] = Finger.create(m, id, 0, n0.findSuccessor(Finger.start(m, id, 0)))
        predecessor = successor.predecessor
        successor.predecessor = this
        for (i in 0 until m - 1) {
            if (Finger.start(m, id, i + 1) in id until (fingers[i]?.node?.id ?: -1))
                fingers[i + 1] = Finger.create(m, id, i, fingers[i]!!.node)
            else
                fingers[i + 1] = Finger.create(m, id, i + 1, n0.findSuccessor(Finger.start(m, id, i + 1)))
        }
    }

    private fun updateOthers() = fingers.forEachIndexed { i, _ ->
        findPredecessor(id - Math.pow(2.0, i.toDouble()).toInt())
                .updateFingerTable(this, i)
    }

    private fun updateFingerTable(s: ChordNode, i: Int) {
        if (s.id in id until (fingers[i]?.node?.id ?: id)) {
            fingers[i] = Finger.create(m, id, i, s)
            predecessor.updateFingerTable(s, i)
        }
    }

    fun remove() {
        predecessor.notifyAboutRemove(this)
        successor.notifyAboutRemove(this)
    }

    private fun notifyAboutRemove(n0: ChordNode) {
        if (predecessor == n0) {
            predecessor = n0.predecessor
        }
        if (successor == n0) {
            successor = n0.successor
            n0.fingers.forEachIndexed { i, _ ->
                findPredecessor(id - Math.pow(2.0, (i - 1).toDouble()).toInt())
                        .updateFingerTable(this, i)
            }
        }
    }

}