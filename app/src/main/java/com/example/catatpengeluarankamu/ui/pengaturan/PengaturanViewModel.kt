package com.example.catatpengeluarankamu.ui.pengaturan

import android.app.Application
import android.content.Context
import android.provider.Settings
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import com.example.catatpengeluarankamu.data.repository.TransaksiRepository

class PengaturanViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
    private val repository = TransaksiRepository(application)

    fun getStatusApp(namaApp: String): Boolean {
        return prefs.getBoolean("aktif_$namaApp", true)
    }

    fun setStatusApp(namaApp: String, aktif: Boolean) {
        prefs.edit().putBoolean("aktif_$namaApp", aktif).apply()
    }

    fun punyaIzinNotifikasi(context: Context): Boolean {
        val listeners = Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners") ?: return false
        return listeners.contains(context.packageName)
    }

    val jumlahBca = liveData { emit(repository.getJumlahTransaksiByApp("myBCA")) }
    val jumlahGopay = liveData { emit(repository.getJumlahTransaksiByApp("GoPay")) }
    val jumlahOvo = liveData { emit(repository.getJumlahTransaksiByApp("OVO")) }
}
