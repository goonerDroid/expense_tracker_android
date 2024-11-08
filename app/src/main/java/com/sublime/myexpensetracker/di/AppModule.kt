package com.sublime.myexpensetracker.di

import android.content.Context
import androidx.room.Room
import com.sublime.myexpensetracker.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase =
        Room
            .databaseBuilder(
                context,
                AppDatabase::class.java,
                "expense_sharing.db",
            ).build()

    @Provides
    fun provideUserDao(database: AppDatabase) = database.userDao()

    @Provides
    fun provideExpenseDao(database: AppDatabase) = database.expenseDao()

    @Provides
    fun provideExpenseSplitDao(database: AppDatabase) = database.expenseSplitDao()
}
