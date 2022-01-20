package io.flaterlab.data.di

import android.content.Context
import com.google.android.gms.nearby.Nearby
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.flaterlab.data.NearbyConnectionFacadeImpl
import io.flaterlab.data.NearbyConnectionsFacade
import java.util.*

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideGson(): Gson = GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .create()

    @Provides
    fun provideNearbyConnectionsFacade(
        @ApplicationContext context: Context,
        gson: Gson,
    ): NearbyConnectionsFacade = NearbyConnectionFacadeImpl(
        userId = UUID.randomUUID().toString(),
        serviceId = context.packageName,
        client = Nearby.getConnectionsClient(context),
        gson = gson,
    )
}