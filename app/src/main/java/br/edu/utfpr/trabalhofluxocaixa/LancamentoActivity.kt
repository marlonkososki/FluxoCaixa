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
import android.app.DatePickerDialog
import java.util.Calendar

class LancamentoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLancamentoBinding

    private var dataSelecionadaMillis: Long = System.currentTimeMillis()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLancamentoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etData.setOnClickListener {
            abrirDatePicker()
        }

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
            data =  dataSelecionadaMillis

        )

        lifecycleScope.launch {
            val dao = AppDatabase.getDatabase(this@LancamentoActivity).lancamentoDao()
            dao.insert(lancamento)
            Toast.makeText(this@LancamentoActivity, "Salvo com sucesso!", Toast.LENGTH_SHORT).show()
            
            // Limpa os campos para um novo lançamento
            binding.etDescricao.text?.clear()
            binding.etValor.text?.clear()
            binding.etDescricao.requestFocus()

            dataSelecionadaMillis = System.currentTimeMillis()
            binding.etData.text?.clear()
        }
    }

    private fun abrirDatePicker() {
        val calendario = Calendar.getInstance()
        calendario.timeInMillis = dataSelecionadaMillis

        val ano = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val calSelecionado = Calendar.getInstance()
                calSelecionado.set(year, month, dayOfMonth, 0, 0, 0)
                calSelecionado.set(Calendar.MILLISECOND, 0)

                dataSelecionadaMillis = calSelecionado.timeInMillis

                val dataFormatada = String.format(
                    "%02d/%02d/%04d",
                    dayOfMonth,
                    month + 1,
                    year
                )

                binding.etData.setText(dataFormatada)
            },
            ano,
            mes,
            dia
        )

        datePickerDialog.show()
    }

}
