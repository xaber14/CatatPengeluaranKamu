package com.example.catatpengeluarankamu.service

import android.content.SharedPreferences
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.example.catatpengeluarankamu.data.model.Transaksi
import com.example.catatpengeluarankamu.data.repository.TransaksiRepository
import com.example.catatpengeluarankamu.util.NotifParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationService : NotificationListenerService() {

    private lateinit var repository: TransaksiRepository
    private lateinit var prefs: SharedPreferences
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        repository = TransaksiRepository(applicationContext)
        prefs = getSharedPreferences("app_settings", MODE_PRIVATE)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val packageName = sbn.packageName ?: return

        // Cek apakah package ini termasuk yang kita pantau
        val namaApp = NotifParser.SUPPORTED_PACKAGES.entries
            .firstOrNull { packageName.startsWith(it.key) }?.value ?: return

        // Cek apakah app ini diaktifkan user di pengaturan
        val isAktif = prefs.getBoolean("aktif_$namaApp", true)
        if (!isAktif) return

        // Ambil teks notifikasi
        val extras = sbn.notification?.extras ?: return
        val judul = extras.getString("android.title") ?: ""
        val isi = extras.getCharSequence("android.text")?.toString() ?: ""

        if (judul.isBlank() && isi.isBlank()) return

        // Parse notifikasi
        val hasil = NotifParser.parse(packageName, judul, isi) ?: return

        // Simpan ke database
        scope.launch {
            val transaksi = Transaksi(
                nominal = hasil.nominal,
                sumberApp = namaApp,
                kategori = hasil.kategori,
                judulNotif = judul,
                isiNotif = isi,
                perluDicek = hasil.perluDicek
            )
            repository.insert(transaksi)
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // Tidak perlu action saat notifikasi dihapus
    }
}
