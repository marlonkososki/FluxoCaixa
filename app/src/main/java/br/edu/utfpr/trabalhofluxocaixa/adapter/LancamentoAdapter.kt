package br.edu.utfpr.trabalhofluxocaixa.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.edu.utfpr.trabalhofluxocaixa.databinding.ItemLancamentoBinding
import br.edu.utfpr.trabalhofluxocaixa.entity.Lancamento
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LancamentoAdapter : ListAdapter<Lancamento, LancamentoAdapter.ViewHolder>(DiffCallback()) {

    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLancamentoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lancamento = getItem(position)
        holder.bind(lancamento)
    }

    inner class ViewHolder(private val binding: ItemLancamentoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(lancamento: Lancamento) {
            binding.tvDescricaoItem.text = lancamento.descricao
            binding.tvDataItem.text = dateFormat.format(Date(lancamento.data))
            binding.tvValorItem.text = currencyFormat.format(lancamento.valor)

            if (lancamento.tipo == "Credito") {
                binding.tvValorItem.setTextColor(Color.parseColor("#4CAF50")) // Verde
            } else {
                binding.tvValorItem.setTextColor(Color.parseColor("#F44336")) // Vermelho
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Lancamento>() {
        override fun areItemsTheSame(oldItem: Lancamento, newItem: Lancamento): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Lancamento, newItem: Lancamento): Boolean {
            return oldItem == newItem
        }
    }
}
