package com.example.catatpengeluarankamu.ui.riwayat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.catatpengeluarankamu.R
import com.example.catatpengeluarankamu.data.model.Transaksi
import com.example.catatpengeluarankamu.databinding.ItemTransaksiBinding
import com.example.catatpengeluarankamu.util.FormatHelper

class TransaksiAdapter(
    private val onItemClick: (Transaksi) -> Unit
) : ListAdapter<Transaksi, TransaksiAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTransaksiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemTransaksiBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transaksi: Transaksi) {
            binding.tvNamaApp.text = transaksi.sumberApp
            binding.tvNominal.text = FormatHelper.formatRupiah(transaksi.nominal)
            binding.tvWaktu.text = "${FormatHelper.formatTanggal(transaksi.timestamp)} · ${transaksi.kategori}"

            // Badge perlu dicek
            binding.badgePerluDicek.isVisible = transaksi.perluDicek

            // Warna background jika perlu dicek
            if (transaksi.perluDicek) {
                binding.root.setBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.kuning_muda)
                )
                binding.tvWaktu.text = "${FormatHelper.formatTanggal(transaksi.timestamp)} · Kategori belum jelas"
                binding.tvWaktu.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.kuning_tua)
                )
            } else {
                binding.root.setBackgroundColor(
                    ContextCompat.getColor(binding.root.context, android.R.color.transparent)
                )
                binding.tvWaktu.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.teks_abu)
                )
            }

            // Icon sumber app
            val iconRes = when (transaksi.sumberApp) {
                "myBCA" -> R.drawable.ic_bank
                "GoPay" -> R.drawable.ic_wallet
                "OVO" -> R.drawable.ic_wallet
                else -> R.drawable.ic_wallet
            }
            binding.ivIconApp.setImageResource(iconRes)

            binding.root.setOnClickListener { onItemClick(transaksi) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Transaksi>() {
        override fun areItemsTheSame(oldItem: Transaksi, newItem: Transaksi) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Transaksi, newItem: Transaksi) = oldItem == newItem
    }
}
