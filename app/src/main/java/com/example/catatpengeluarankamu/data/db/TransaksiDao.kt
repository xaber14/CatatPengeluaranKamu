package com.example.catatpengeluarankamu.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.catatpengeluarankamu.data.model.Transaksi

@Dao
interface TransaksiDao {

    @Insert
    suspend fun insert(transaksi: Transaksi): Long

    @Update
    suspend fun update(transaksi: Transaksi)

    @Delete
    suspend fun delete(transaksi: Transaksi)

    // Semua transaksi, urut terbaru
    @Query("SELECT * FROM transaksi ORDER BY timestamp DESC")
    fun getAllTransaksi(): LiveData<List<Transaksi>>

    // Transaksi per sumber app
    @Query("SELECT * FROM transaksi WHERE sumberApp = :app ORDER BY timestamp DESC")
    fun getTransaksiByApp(app: String): LiveData<List<Transaksi>>

    // Total pengeluaran bulan ini
    @Query("""
        SELECT COALESCE(SUM(nominal), 0) FROM transaksi 
        WHERE strftime('%Y-%m', datetime(timestamp/1000, 'unixepoch')) = strftime('%Y-%m', 'now')
    """)
    fun getTotalBulanIni(): LiveData<Long>

    // Total pengeluaran bulan lalu
    @Query("""
        SELECT COALESCE(SUM(nominal), 0) FROM transaksi 
        WHERE strftime('%Y-%m', datetime(timestamp/1000, 'unixepoch')) = strftime('%Y-%m', datetime('now', '-1 month'))
    """)
    fun getTotalBulanLalu(): LiveData<Long>

    // Transaksi bulan ini saja (untuk breakdown kategori)
    @Query("""
        SELECT * FROM transaksi 
        WHERE strftime('%Y-%m', datetime(timestamp/1000, 'unixepoch')) = strftime('%Y-%m', 'now')
        ORDER BY timestamp DESC
    """)
    fun getTransaksiBulanIni(): LiveData<List<Transaksi>>

    // Jumlah transaksi per app (untuk pengaturan)
    @Query("SELECT COUNT(*) FROM transaksi WHERE sumberApp = :app")
    suspend fun getJumlahTransaksiByApp(app: String): Int
}
