package com.adyen.android.assignment.di

import android.content.Context
import com.adyen.android.assignment.BuildConfig
import com.adyen.android.assignment.data.api.query.VenueRecommendationsQueryBuilder
import com.adyen.android.assignment.data.api.repository.PlacesRepository
import com.adyen.android.assignment.data.api.service.PlacesService
import com.adyen.android.assignment.data.location.LocationProvider
import com.adyen.android.assignment.data.location.LocationRepository
import com.adyen.android.assignment.domain.model.Mapper
import com.adyen.android.assignment.domain.usecase.GetLocationStatusUseCase
import com.adyen.android.assignment.domain.usecase.GetNearbyPlacesUseCase
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NearbyModule {

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
        .baseUrl(BuildConfig.FOURSQUARE_BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun providePlacesService(retrofit: Retrofit): PlacesService = retrofit.create(PlacesService::class.java)

    @Singleton
    @Provides
    fun providesPlacesRepository(placesService: PlacesService) = PlacesRepository(placesService)

    @Singleton
    @Provides
    fun providesLocationProvider(@ApplicationContext appContext: Context) = LocationProvider(
        LocationServices.getFusedLocationProviderClient(appContext)
    )

    @Singleton
    @Provides
    fun providesLocationRepository(
        locationProvider: LocationProvider,
        @ApplicationContext appContext: Context
    ) = LocationRepository(locationProvider, appContext)

    @Provides
    fun providesNearbyPlacesUseCase(
        locationRepository: LocationRepository,
        placesRepository: PlacesRepository
    ) = GetNearbyPlacesUseCase(
        locationRepository = locationRepository,
        placesRepository = placesRepository,
        queryBuilder = VenueRecommendationsQueryBuilder(),
        mapper = Mapper()
    )

    @Provides
    fun providesLocationStatusUseCase(
        locationRepository: LocationRepository
    ) = GetLocationStatusUseCase(
        locationRepository = locationRepository
    )
}