package com.example.catatpengeluarankamu.data.repository

import android.content.Context
import com.example.catatpengeluarankamu.data.db.AppDatabase
import com.example.catatpengeluarankamu.data.model.Transaksi

class TransaksiRepository(context: Context) {

    private val dao = AppDatabase.getDatabase(context).transaksiDao()

    val allTransaksi = dao.getAllTransaksi()
    val totalBulanIni = dao.getTotalBulanIni()
    val totalBulanLalu = dao.getTotalBulanLalu()
    val transaksiBulanIni = dao.getTransaksiBulanIni()

    fun getTransaksiByApp(app: String) = dao.getTransaksiByApp(app)

    suspend fun insert(transaksi: Transaksi) = dao.insert(transaksi)

    suspend fun update(transaksi: Transaksi) = dao.update(transaksi)

    suspend fun delete(transaksi: Transaksi) = dao.delete(transaksi)

    suspend fun getJumlahTransaksiByApp(app: String) = dao.getJumlahTransaksiByApp(app)
}
