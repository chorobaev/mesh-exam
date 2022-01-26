package io.flaterlab.meshexam.library.nearby.di

import android.content.Context
import com.google.android.gms.nearby.Nearby
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.flaterlab.meshexam.library.nearby.api.NearbyFacade
import io.flaterlab.meshexam.library.nearby.impl.AdvertiserInfoJsonParser
import io.flaterlab.meshexam.library.nearby.impl.ClientInfoJsonParser
import io.flaterlab.meshexam.library.nearby.impl.NearbyFacadeImpl
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
internal class NearbyModule {

    @Provides
    fun provideNearbyFacade(
        @ApplicationContext context: Context,
        advertiserInfoJsonParser: AdvertiserInfoJsonParser,
        clientInfoJsonParser: ClientInfoJsonParser,
    ): NearbyFacade =
        NearbyFacadeImpl(
            serviceId = context.packageName,
            client = Nearby.getConnectionsClient(context),
            advertiserInfoParser = advertiserInfoJsonParser,
            clientInfoParser = clientInfoJsonParser
        )
}