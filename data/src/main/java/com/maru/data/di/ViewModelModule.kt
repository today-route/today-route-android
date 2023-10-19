package com.maru.data.di

import com.maru.data.datasource.route.RouteDataSource
import com.maru.data.datasource.route.RouteRemoteDataSource
import com.maru.data.datasource.token.TokenDataSource
import com.maru.data.datasource.token.TokenLocalDataSource
import com.maru.data.datasource.token.TokenRemoteDataSource
import com.maru.data.datasource.user.UserDataSource
import com.maru.data.datasource.user.UserRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {

    @ViewModelScoped
    @Binds
    abstract fun bindTokenLocalDataSource(tokenLocalDataSource: TokenLocalDataSource): TokenDataSource.Local

    @ViewModelScoped
    @Binds
    abstract fun bindTokenRemoteDataSource(tokenRemoteDataSource: TokenRemoteDataSource): TokenDataSource.Remote

    @ViewModelScoped
    @Binds
    abstract fun bindRouteDataSource(routeDataSource: RouteRemoteDataSource): RouteDataSource

    @ViewModelScoped
    @Binds
    abstract fun bindUserDataSource(userDataSource: UserRemoteDataSource): UserDataSource
}