package br.edu.utfpr.trabalhofluxocaixa.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.edu.utfpr.trabalhofluxocaixa.dao.LancamentoDao
import br.edu.utfpr.trabalhofluxocaixa.entity.Lancamento

@Database(entities = [Lancamento::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun lancamentoDao(): LancamentoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fluxo_caixa_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
