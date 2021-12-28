package com.kasa.dagger

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.kasa.dao.ProductDao
import com.kasa.databases.DB
import com.kasa.network.ApiService
import com.kasa.network.CustomAuthenticator
import com.kasa.network.RetrofitBuilder
import com.kasa.utils.Constants.DB_NAME

import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule() {


    @Provides
    @Singleton
    fun provideContext(app: Application): Context {

        return app.applicationContext

    }


    @Singleton
    @Provides
    @Named("auth")
    fun provideApiServiceWithAuth(retrofitBuilder: RetrofitBuilder,
                                  authenticator: CustomAuthenticator
                                  ): ApiService {
        return retrofitBuilder
            .createServiceWithAuth(ApiService::class.java, authenticator)

    }


    @Singleton
    @Provides
    @Named("no_auth")
    fun provideApiService(retrofitBuilder: RetrofitBuilder): ApiService {
        return retrofitBuilder.createService(ApiService::class.java)

    }


    @Singleton
    @Provides
    fun provideDb(app: Application): DB {
        return Room
            .databaseBuilder(app, DB::class.java, DB_NAME)
//            .createFromAsset(DB_PATH)
//            .addMigrations(GIR_MIGRATION_2_3)
//            .addMigrations(GIR_MIGRATION_3_4)
            .fallbackToDestructiveMigration()
            .build()
    }


    @Singleton
    @Provides
    fun provideMessageDao(db: DB): ProductDao {
        return db.productDao()
    }


}


