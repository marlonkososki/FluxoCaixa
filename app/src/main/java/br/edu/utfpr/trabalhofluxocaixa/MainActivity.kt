package br.edu.utfpr.trabalhofluxocaixa

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.edu.utfpr.trabalhofluxocaixa.adapter.LancamentoAdapter
import br.edu.utfpr.trabalhofluxocaixa.database.AppDatabase
import br.edu.utfpr.trabalhofluxocaixa.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: LancamentoAdapter
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarRecyclerView()
        configurarResumo()

        binding.fabAdicionar.setOnClickListener {
            val intent = Intent(this, LancamentoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun configurarRecyclerView() {
        adapter = LancamentoAdapter()
        binding.rvLancamentos.adapter = adapter

        val dao = AppDatabase.getDatabase(this).lancamentoDao()
        
        // Observa a lista de lançamentos e atualiza o RecyclerView
        lifecycleScope.launch {
            dao.getAll().collect { lista ->
                adapter.submitList(lista)
            }
        }
    }

    private fun configurarResumo() {
        val dao = AppDatabase.getDatabase(this).lancamentoDao()

        lifecycleScope.launch {
            combine(dao.getTotalCredito(), dao.getTotalDebito()) { credito, debito ->
                val totalCredito = credito ?: 0.0
                val totalDebito = debito ?: 0.0
                val saldo = totalCredito - totalDebito
                
                Triple(totalCredito, totalDebito, saldo)
            }.collect { (credito, debito, saldo) ->
                binding.tvEntradas.text = currencyFormat.format(credito)
                binding.tvSaidas.text = currencyFormat.format(debito)
                binding.tvSaldo.text = currencyFormat.format(saldo)
            }
        }
    }
}
