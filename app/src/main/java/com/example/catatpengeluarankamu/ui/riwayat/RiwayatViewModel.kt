package com.example.catatpengeluarankamu.ui.riwayat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.catatpengeluarankamu.data.repository.TransaksiRepository

class RiwayatViewModel(application: Application) : AndroidViewModel(application) {

    val repository = TransaksiRepository(application)
    val allTransaksi = repository.allTransaksi

    fun getTransaksiByApp(app: String) = repository.getTransaksiByApp(app)
}
