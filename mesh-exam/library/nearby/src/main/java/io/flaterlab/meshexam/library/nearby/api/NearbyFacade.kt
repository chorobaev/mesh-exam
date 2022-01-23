package io.flaterlab.meshexam.library.nearby.api

import io.flaterlab.meshexam.library.nearby.impl.dto.AdvertiserInfo

interface NearbyFacade {

    suspend fun advertise(advertiserInfo: AdvertiserInfo)

    fun stopAdvertising()

    suspend fun discover()

    fun stopDiscovery()
}