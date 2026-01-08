package br.edu.utfpr.trabalhofluxocaixa.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lancamentos")
data class Lancamento(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val descricao: String,
    val valor: Double,
    val tipo: String, // "Credito" ou "Debito"
    val data: Long
)
