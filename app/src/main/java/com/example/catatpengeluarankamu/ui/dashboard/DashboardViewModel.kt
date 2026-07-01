package com.example.catatpengeluarankamu.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.catatpengeluarankamu.data.repository.TransaksiRepository

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    val repository = TransaksiRepository(application)

    val totalBulanIni = repository.totalBulanIni
    val totalBulanLalu = repository.totalBulanLalu
    val transaksiBulanIni = repository.transaksiBulanIni
    val transaksiTerbaru = repository.allTransaksi
}
