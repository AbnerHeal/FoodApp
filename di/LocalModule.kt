package com.example.foodapp.di

import android.content.Context
import androidx.room.Room
import com.example.foodapp.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//HILT proporciona contenedores para cada clase y administra sus ciclos de vida de forma automática

@Module //indicamos a hilt que es un modulo que usaremos para ROOM, el arroba es un decorador
@InstallIn(SingletonComponent::class)//decinmos que trabajaremos con singleton,
// ahi se instalara el modulo de inyeccion de deopendencias
// singleton component donde habra instancias
class LocalModule {
    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "appFood_db"
        ).build()
    //DICCIONARIO DE DEPENDENCIAS

    //creamos instancias por cada DAO (provee de funciones) para ROOM
    //con hilt normalmente es un singleton
    @Provides
    @Singleton
    fun provideFoodDao(appDatabase: AppDatabase) = appDatabase.foodDao() //cuando lo llamamos devuelve una instancia de foodDao (para poder insertar, leer, remplazar, borrar)

    @Provides
    @Singleton
    fun provideDrinkDao(appDatabase: AppDatabase) = appDatabase.drinkDao()//instancia para proveer de los metodos de Dao, desde el data base

    @Provides
    @Singleton
    fun provideDessertDao(appDatabase: AppDatabase) = appDatabase.dessertDao()

    @Provides
    @Singleton
    fun provideFlavorDao(appDatabase: AppDatabase)= appDatabase.flavorDao()

    @Provides
    @Singleton
    fun provideSupplementDao(appDatabase: AppDatabase)=appDatabase.supplementDao()

    @Provides
    @Singleton
    fun provideCheckoutDao(appDatabase: AppDatabase)=appDatabase.checkoutDao()
}