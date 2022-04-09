package com.adyen.android.assignment.di

import com.adyen.android.assignment.BuildConfig
import com.adyen.android.assignment.api.repository.PlacesRepositoryImpl
import com.adyen.android.assignment.api.service.PlacesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NearbyModule {

    private const val BASE_URL = BuildConfig.FOURSQUARE_BASE_URL

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() =
        HttpLoggingInterceptor()
            .apply {
                level =
                    if (BuildConfig.DEBUG)
                        HttpLoggingInterceptor.Level.BODY
                    else
                        HttpLoggingInterceptor.Level.NONE
            }

    @Singleton
    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor) = OkHttpClient
        .Builder()
        .addInterceptor(httpLoggingInterceptor)
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun providePlacesService(retrofit: Retrofit): PlacesService = retrofit.create(PlacesService::class.java)

    @Singleton
    @Provides
    fun providesPlacesRepository(placesService: PlacesService) = PlacesRepositoryImpl(placesService)
}