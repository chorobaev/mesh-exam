package io.flaterlab.meshexam.librariy.mesh.host

import java.util.*

internal class MeshHand(
    private val count: Int = 2,
) {

    private val _children: Queue<String> = LinkedList()

    fun addEndpointId(endpointId: String) {
        _children.offer(endpointId)
        if (_children.size > count) _children.remove()
    }

    fun removeEndpointId(endpointId: String) {
        _children.remove(endpointId)
    }

    fun asList() = _children.toList()
}