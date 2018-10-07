data class ChordNode(
        val m: Int,
        val id: Int
) {

    private val successor
        get() = fingers[0].node

    private var predecessor = this

    val fingers: MutableList<Finger> = MutableList(m) {
        Finger.create(m, id, it, this)
    }

    fun findSuccessor(id: Int) = findPredecessor(id).successor

    private fun findPredecessor(id: Int): ChordNode {
        var p = this
        while (!id.belongs(p.id, p.successor.id, includeSecond = true)) {
            p = p.closestPrecedingFinger(id)
        }
        return p
    }

    private fun closestPrecedingFinger(id: Int): ChordNode {
        return (fingers.size - 1 downTo 0)
                .firstOrNull { fingers[it].node.id.belongs(this.id, id) }
                ?.let { fingers[it].node }
                ?: this
    }

    fun join(n0: ChordNode?) {
        if (n0 != null) {
            initFingerTable(n0)
            updateOthers()
        }
    }

    private fun initFingerTable(n0: ChordNode) {
        fingers[0] = Finger.create(m, id, 0, n0.findSuccessor(fingers[0].start))
        predecessor = successor.predecessor
        successor.predecessor = this
        for (i in 0 until fingers.size - 1) {
            if (fingers[i + 1].start.belongs(id, fingers[i].node.id, includeFirst = true)) {
                fingers[i + 1] = Finger.create(m, id, i + 1, fingers[i].node)
            } else {
                fingers[i + 1] = Finger.create(m, id, i + 1, n0.findSuccessor(fingers[i + 1].start))
            }
        }
    }

    private fun updateOthers() {
        for (i in 0 until fingers.size) {
            val index = ((id - Math.pow(2.0, (i - 1).toDouble()).toInt()) + Math.pow(2.0, m.toDouble()).toInt()) % Math.pow(2.0, m.toDouble()).toInt()
            findPredecessor(index).updateFingerTable(this, i)
        }
    }

    private fun updateFingerTable(s: ChordNode, i: Int) {
        if (s.id.belongs(id, fingers[i].node.id, includeFirst = true)) {
            fingers[i] = Finger.create(m, id, i, s)
            predecessor.updateFingerTable(s, i)
        }
    }

    fun remove() {
        //todo реализовать
    }

    private fun Int.belongs(
            firstId: Int,
            secondId: Int,
            includeFirst: Boolean = false,
            includeSecond: Boolean = false
    ): Boolean {
        if (firstId < secondId) {
            val first = if (includeFirst) firstId else firstId + 1
            val second = if (includeSecond) secondId else secondId - 1
            return this in first..second
        } else {
            val first = (if (includeFirst) firstId else firstId + 1) % Math.pow(2.0, m.toDouble()).toInt()
            val second = ((if (includeSecond) secondId else secondId - 1) + Math.pow(2.0, m.toDouble()).toInt()) % Math.pow(2.0, m.toDouble()).toInt()
            if (first == second) {
                return true
            } else if (first < second) {
                return this in first..second
            } else if (first > second) {
                return this in first until Math.pow(2.0, m.toDouble()).toInt() || this in 0..second
            }
        }


        //todo Функция принадлежности айдишника на кольце
        return true
    }


}