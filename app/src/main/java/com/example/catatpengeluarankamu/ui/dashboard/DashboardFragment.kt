package com.example.catatpengeluarankamu.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.catatpengeluarankamu.R
import com.example.catatpengeluarankamu.databinding.FragmentDashboardBinding
import com.example.catatpengeluarankamu.ui.riwayat.TransaksiAdapter
import com.example.catatpengeluarankamu.ui.riwayat.EditTransaksiDialog
import com.example.catatpengeluarankamu.util.FormatHelper

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DashboardViewModel
    private lateinit var adapter: TransaksiAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[DashboardViewModel::class.java]

        setupAdapter()
        observeData()
    }

    private fun setupAdapter() {
        adapter = TransaksiAdapter { transaksi ->
            EditTransaksiDialog(transaksi, viewModel.repository).show(parentFragmentManager, "edit")
        }
        binding.rvTransaksiTerbaru.adapter = adapter
    }

    private fun observeData() {
        viewModel.totalBulanIni.observe(viewLifecycleOwner) { total ->
            binding.tvTotalBulanIni.text = FormatHelper.formatRupiah(total ?: 0)
        }

        viewModel.totalBulanLalu.observe(viewLifecycleOwner) { bulanLalu ->
            val bulanIni = viewModel.totalBulanIni.value ?: 0
            val bulanLaluVal = bulanLalu ?: 0
            val persen = FormatHelper.hitungPersentase(bulanIni, bulanLaluVal)
            val naik = FormatHelper.isNaik(bulanIni, bulanLaluVal)

            binding.tvPerbandingan.text = persen
            binding.tvPerbandingan.setTextColor(
                ContextCompat.getColor(requireContext(),
                    if (naik) R.color.merah else R.color.hijau)
            )
        }

        viewModel.transaksiBulanIni.observe(viewLifecycleOwner) { list ->
            // Hitung breakdown per kategori
            val breakdown = list.groupBy { it.kategori }
                .mapValues { entry -> entry.value.sumOf { it.nominal } }
                .toList()
                .sortedByDescending { it.second }

            val total = list.sumOf { it.nominal }.coerceAtLeast(1)

            binding.layoutKategori.removeAllViews()
            breakdown.forEach { (kategori, jumlah) ->
                val persen = (jumlah.toFloat() / total * 100).toInt()
                val itemView = layoutInflater.inflate(R.layout.item_kategori, binding.layoutKategori, false)
                itemView.findViewById<android.widget.TextView>(R.id.tv_nama_kategori).text = kategori
                itemView.findViewById<android.widget.TextView>(R.id.tv_jumlah_kategori).text = FormatHelper.formatRupiah(jumlah)
                itemView.findViewById<android.widget.ProgressBar>(R.id.progress_kategori).progress = persen
                binding.layoutKategori.addView(itemView)
            }
        }

        viewModel.transaksiTerbaru.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list.take(5))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
