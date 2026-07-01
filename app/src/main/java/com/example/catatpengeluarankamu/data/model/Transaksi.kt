package com.example.catatpengeluarankamu.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaksi")
data class Transaksi(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nominal: Long,                    // nominal dalam rupiah (tanpa desimal)
    val sumberApp: String,                // "myBCA", "GoPay", "OVO"
    val kategori: String,                 // "Makan & Minum", "Transport", dll
    val catatan: String = "",             // catatan manual dari user
    val judulNotif: String = "",          // judul notifikasi asli (untuk referensi)
    val isiNotif: String = "",            // isi notifikasi asli (untuk referensi)
    val timestamp: Long = System.currentTimeMillis(),
    val perluDicek: Boolean = false,      // true jika parsing tidak yakin
    val sudahDiedit: Boolean = false      // true jika user sudah edit manual
)
