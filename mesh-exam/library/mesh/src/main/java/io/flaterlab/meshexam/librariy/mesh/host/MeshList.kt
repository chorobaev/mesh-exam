package io.flaterlab.meshexam.librariy.mesh.host

import io.flaterlab.meshexam.librariy.mesh.common.dto.ClientInfo
import java.util.*

internal class MeshList(
    private val inner: LinkedList<ClientInfo> = LinkedList(),
    private val isPositiveDirection: Boolean,
) : List<ClientInfo> by inner {

    fun add(client: ClientInfo, parentId: String? = null) {
        while (parentId != null && inner.isNotEmpty() && inner.last.id != parentId) {
            inner.removeLast()
        }
        val position = size.inc() * if (isPositiveDirection) 1 else -1
        inner.add(client.copy(positionInMesh = position))
    }

    fun remove(client: ClientInfo) {
        if (inner.any { it.id == client.id }) {
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