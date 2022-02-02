package io.flaterlab.meshexam.librariy.mesh.host

import io.flaterlab.meshexam.librariy.mesh.common.dto.ClientInfo
import java.util.*

internal class MeshList(
    private val inner: LinkedList<ClientInfo> = LinkedList()
) : List<ClientInfo> by inner {

    fun add(client: ClientInfo, parentId: String? = null) {
        while (inner.isNotEmpty() && inner.last.id != parentId) {
            inner.removeLast()
        }
        inner.add(client)
    }

    fun remove(client: ClientInfo) {
        if (inner.contains(client)) {
            while (inner.isNotEmpty() && inner.last != client) {
                inner.removeLast()
            }
            if (inner.isNotEmpty()) inner.removeLast()
        }
    }

    fun mergeByClosest(other: List<ClientInfo>): List<ClientInfo> = buildList {
        val selfI = this@MeshList.iterator()
        val otherI = other.iterator()
        while (selfI.hasNext() || otherI.hasNext()) {
            if (selfI.hasNext()) add(selfI.next())
            if (otherI.hasNext()) add(otherI.next())
        }
    }

    fun clear() {
        inner.clear()
    }

    override fun toString(): String {
        return joinToString(prefix = "[", postfix = "]") { it.toString() }
    }
}