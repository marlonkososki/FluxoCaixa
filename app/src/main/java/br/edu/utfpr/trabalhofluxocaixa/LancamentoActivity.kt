package br.edu.utfpr.trabalhofluxocaixa

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.edu.utfpr.trabalhofluxocaixa.database.AppDatabase
import br.edu.utfpr.trabalhofluxocaixa.databinding.ActivityLancamentoBinding
import br.edu.utfpr.trabalhofluxocaixa.entity.Lancamento
import kotlinx.coroutines.launch

class LancamentoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLancamentoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLancamentoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Botão para salvar o lançamento
        binding.btnSalvar.setOnClickListener {
            salvarLancamento()
        }

        // Botão para navegar até o extrato (MainActivity)
        binding.btnVerExtrato.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun salvarLancamento() {
        val descricao = binding.etDescricao.text.toString()
        val valorStr = binding.etValor.text.toString()
        val tipo = if (binding.rbCredito.isChecked) "Credito" else "Debito"

        if (descricao.isEmpty() || valorStr.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }

        val valor = valorStr.toDoubleOrNull() ?: 0.0
        val lancamento = Lancamento(
            descricao = descricao,
            valor = valor,
            tipo = tipo,
            data = System.currentTimeMillis()
        )

        lifecycleScope.launch {
            val dao = AppDatabase.getDatabase(this@LancamentoActivity).lancamentoDao()
            dao.insert(lancamento)
            Toast.makeText(this@LancamentoActivity, "Salvo com sucesso!", Toast.LENGTH_SHORT).show()
            
            // Limpa os campos para um novo lançamento
            binding.etDescricao.text?.clear()
            binding.etValor.text?.clear()
            binding.etDescricao.requestFocus()
        }
    }
}
