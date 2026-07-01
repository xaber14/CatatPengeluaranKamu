package com.example.catatpengeluarankamu.ui.riwayat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.catatpengeluarankamu.R
import com.example.catatpengeluarankamu.databinding.FragmentRiwayatBinding

class RiwayatFragment : Fragment() {

    private var _binding: FragmentRiwayatBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: RiwayatViewModel
    private lateinit var adapter: TransaksiAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRiwayatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[RiwayatViewModel::class.java]

        adapter = TransaksiAdapter { transaksi ->
            EditTransaksiDialog(transaksi, viewModel.repository).show(parentFragmentManager, "edit")
        }
        binding.rvRiwayat.adapter = adapter

        // Filter chips
        var filterAktif = "Semua"
        val chips = mapOf(
            binding.chipSemua to "Semua",
            binding.chipBca to "myBCA",
            binding.chipGopay to "GoPay",
            binding.chipOvo to "OVO"
        )

        chips.forEach { (chip, label) ->
            chip.setOnClickListener {
                filterAktif = label
                chips.keys.forEach { c -> c.isChecked = false }
                chip.isChecked = true
                applyFilter(filterAktif)
            }
        }
        binding.chipSemua.isChecked = true

        // Observe semua transaksi
        viewModel.allTransaksi.observe(viewLifecycleOwner) {
            applyFilter(filterAktif)
        }
    }

    private fun applyFilter(filter: String) {
        if (filter == "Semua") {
            viewModel.allTransaksi.value?.let { adapter.submitList(it) }
        } else {
            viewModel.getTransaksiByApp(filter).observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
