package br.edu.utfpr.trabalhofluxocaixa.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import br.edu.utfpr.trabalhofluxocaixa.entity.Lancamento
import kotlinx.coroutines.flow.Flow

@Dao
interface LancamentoDao {

    @Insert
    suspend fun insert(lancamento: Lancamento)

    @Delete
    suspend fun delete(lancamento: Lancamento)

    @Query("SELECT * FROM lancamentos ORDER BY data DESC")
    fun getAll(): Flow<List<Lancamento>>

    @Query("SELECT SUM(valor) FROM lancamentos WHERE tipo = 'Credito'")
    fun getTotalCredito(): Flow<Double?>

    @Query("SELECT SUM(valor) FROM lancamentos WHERE tipo = 'Debito'")
    fun getTotalDebito(): Flow<Double?>
}
