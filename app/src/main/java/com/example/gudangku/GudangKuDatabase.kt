package com.example.gudangku

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        TableUser::class,
        TableGudang::class,
        TableBarang::class,
        TablePersediaan::class,
        TableRiwayat::class

    ],
    version = 6
)
abstract class GudangKuDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun gudangDao(): GudangDao
    abstract fun barangDao(): BarangDao
    abstract fun persediaanDao(): PersediaanDao
    abstract fun riwayatDao(): RiwayatDao

    companion object {
        @Volatile
        private var INSTANCE: GudangKuDatabase? = null

        fun getInstance(context: Context): GudangKuDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    GudangKuDatabase::class.java,
                    "gudangku_db"
                )
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
        }
    }
}
