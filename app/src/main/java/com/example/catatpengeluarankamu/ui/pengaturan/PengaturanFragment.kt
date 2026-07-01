package com.example.catatpengeluarankamu.ui.pengaturan

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.catatpengeluarankamu.databinding.FragmentPengaturanBinding

class PengaturanFragment : Fragment() {

    private var _binding: FragmentPengaturanBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PengaturanViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPengaturanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[PengaturanViewModel::class.java]

        setupToggle()
        setupNotifAccess()
        loadJumlahTransaksi()
    }

    override fun onResume() {
        super.onResume()
        // Cek ulang status izin notifikasi setiap kali halaman ini aktif
        setupNotifAccess()
    }

    private fun setupNotifAccess() {
        val punya = viewModel.punyaIzinNotifikasi(requireContext())
        binding.layoutWarningNotif.isVisible = !punya
        binding.btnBukaSettings.isVisible = !punya

        binding.btnBukaSettings.setOnClickListener {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }
    }

    private fun setupToggle() {
        // Load status toggle dari SharedPreferences
        binding.switchBca.isChecked = viewModel.getStatusApp("myBCA")
        binding.switchGopay.isChecked = viewModel.getStatusApp("GoPay")
        binding.switchOvo.isChecked = viewModel.getStatusApp("OVO")

        binding.switchBca.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setStatusApp("myBCA", isChecked)
        }
        binding.switchGopay.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setStatusApp("GoPay", isChecked)
        }
        binding.switchOvo.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setStatusApp("OVO", isChecked)
        }
    }

    private fun loadJumlahTransaksi() {
        viewModel.jumlahBca.observe(viewLifecycleOwner) {
            binding.tvInfoBca.text = if (it > 0) "Terhubung · $it transaksi tercatat" else "Terhubung · Belum ada transaksi"
        }
        viewModel.jumlahGopay.observe(viewLifecycleOwner) {
            binding.tvInfoGopay.text = if (it > 0) "Terhubung · $it transaksi tercatat" else "Terhubung · Belum ada transaksi"
        }
        viewModel.jumlahOvo.observe(viewLifecycleOwner) {
            binding.tvInfoOvo.text = if (it > 0) "Terhubung · $it transaksi tercatat" else "Terhubung · Belum ada transaksi"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
