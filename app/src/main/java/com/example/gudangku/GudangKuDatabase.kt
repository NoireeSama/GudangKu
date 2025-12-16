package com.example.gudangku

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        TableGudang::class,
        TableBarang::class,
        TablePersediaan::class,
        TableRiwayat::class
    ],
    version = 1
)
abstract class GudangKuDatabase : RoomDatabase() {

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
                ).build().also { INSTANCE = it }
            }
        }
    }
}
