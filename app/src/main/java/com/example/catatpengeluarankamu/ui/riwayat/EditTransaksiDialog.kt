package com.example.catatpengeluarankamu.ui.riwayat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.catatpengeluarankamu.R
import com.example.catatpengeluarankamu.data.model.Transaksi
import com.example.catatpengeluarankamu.data.repository.TransaksiRepository
import com.example.catatpengeluarankamu.databinding.DialogEditTransaksiBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

class EditTransaksiDialog(
    private val transaksi: Transaksi,
    private val repository: TransaksiRepository
) : BottomSheetDialogFragment() {

    private var _binding: DialogEditTransaksiBinding? = null
    private val binding get() = _binding!!

    private val daftarKategori = listOf(
        "Makan & Minum", "Transport", "Belanja", "Tagihan",
        "Hiburan", "Kesehatan", "Lainnya"
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DialogEditTransaksiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Isi data existing
        binding.etNominal.setText(transaksi.nominal.toString())
        binding.etCatatan.setText(transaksi.catatan)
        binding.tvSumber.text = "${transaksi.sumberApp} · ${formatWaktu(transaksi.timestamp)}"

        // Tampilkan warning jika perlu dicek
        binding.layoutPerluDicek.isVisible = transaksi.perluDicek

        // Setup dropdown kategori
        val adapterKategori = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, daftarKategori)
        binding.spinnerKategori.adapter = adapterKategori
        val indexKategori = daftarKategori.indexOf(transaksi.kategori)
        if (indexKategori >= 0) binding.spinnerKategori.setSelection(indexKategori)

        // Tombol simpan
        binding.btnSimpan.setOnClickListener {
            val nominalBaru = binding.etNominal.text.toString().toLongOrNull() ?: transaksi.nominal
            val kategoriBaru = binding.spinnerKategori.selectedItem.toString()
            val catatanBaru = binding.etCatatan.text.toString()

            val transaksiUpdate = transaksi.copy(
                nominal = nominalBaru,
                kategori = kategoriBaru,
                catatan = catatanBaru,
                perluDicek = false,
                sudahDiedit = true
            )

            lifecycleScope.launch {
                repository.update(transaksiUpdate)
                dismiss()
            }
        }

        // Tombol hapus
        binding.btnHapus.setOnClickListener {
            lifecycleScope.launch {
                repository.delete(transaksi)
                dismiss()
            }
        }

        // Tombol tutup
        binding.btnTutup.setOnClickListener { dismiss() }
    }

    private fun formatWaktu(timestamp: Long): String {
        val sdf = java.text.SimpleDateFormat("dd MMM, HH:mm", java.util.Locale("id", "ID"))
        return sdf.format(java.util.Date(timestamp))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
