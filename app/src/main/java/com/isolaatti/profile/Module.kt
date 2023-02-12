package com.isolaatti.profile

import com.isolaatti.connectivity.RetrofitClient
import com.isolaatti.profile.data.remote.ProfileApi
import com.isolaatti.profile.data.repository.ProfileRepositoryImpl
import com.isolaatti.profile.domain.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn

@Module
@InstallIn
class Module {
    @Provides
    fun provideProfileApi(retrofitClient: RetrofitClient): ProfileApi {
        return retrofitClient.client.create(ProfileApi::class.java)
    }

    @Provides
    fun provideProfileRepository(profileApi: ProfileApi): ProfileRepository {
        return ProfileRepositoryImpl(profileApi)
    }
}