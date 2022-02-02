package io.flaterlab.meshexam.librariy.mesh.client

import io.flaterlab.meshexam.librariy.mesh.common.dto.AdvertiserInfo
import java.util.*

internal class AdvertiserInfoCache {

    private val map = TreeMap<String, AdvertiserInfo>()

    operator fun set(endpointId: String, advertiserInfo: AdvertiserInfo) {
        map[endpointId] = advertiserInfo
    }

    operator fun get(endpointId: String): AdvertiserInfo? {
        return map[endpointId]
    }

    fun getUniqueListByExamId(): List<AdvertiserInfo> {
        return map.map { it.value }.distinctBy { it.examId }
    }

    fun getEndpointByExamId(examId: String): String? {
        return map.entries.find { it.value.examId == examId }?.key
    }

    fun remove(endpointId: String): AdvertiserInfo? {
        return map.remove(endpointId)
    }

    fun clear() {
        map.clear()
    }

    override fun toString(): String {
        return map.toString()
    }
}