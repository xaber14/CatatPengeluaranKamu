package com.example.catatpengeluarankamu.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object FormatHelper {

    fun formatRupiah(nominal: Long): String {
        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        format.maximumFractionDigits = 0
        return format.format(nominal)
    }

    fun formatTanggal(timestamp: Long): String {
        val sekarang = Calendar.getInstance()
        val waktu = Calendar.getInstance().apply { timeInMillis = timestamp }

        return when {
            isSameDay(sekarang, waktu) -> {
                val sdf = SimpleDateFormat("HH:mm", Locale("id", "ID"))
                "Hari ini, ${sdf.format(Date(timestamp))}"
            }
            isYesterday(sekarang, waktu) -> {
                val sdf = SimpleDateFormat("HH:mm", Locale("id", "ID"))
                "Kemarin, ${sdf.format(Date(timestamp))}"
            }
            else -> {
                val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
                sdf.format(Date(timestamp))
            }
        }
    }

    fun formatTanggalGrup(timestamp: Long): String {
        val sekarang = Calendar.getInstance()
        val waktu = Calendar.getInstance().apply { timeInMillis = timestamp }

        return when {
            isSameDay(sekarang, waktu) -> "Hari ini"
            isYesterday(sekarang, waktu) -> "Kemarin"
            else -> {
                val sdf = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
                sdf.format(Date(timestamp))
            }
        }
    }

    private fun isSameDay(a: Calendar, b: Calendar): Boolean {
        return a.get(Calendar.YEAR) == b.get(Calendar.YEAR) &&
                a.get(Calendar.DAY_OF_YEAR) == b.get(Calendar.DAY_OF_YEAR)
    }

    private fun isYesterday(sekarang: Calendar, waktu: Calendar): Boolean {
        val kemarin = Calendar.getInstance().apply {
            timeInMillis = sekarang.timeInMillis
            add(Calendar.DAY_OF_YEAR, -1)
        }
        return isSameDay(kemarin, waktu)
    }

    fun hitungPersentase(bulanIni: Long, bulanLalu: Long): String {
        if (bulanLalu == 0L) return "Bulan pertama"
        val selisih = bulanIni - bulanLalu
        val persen = (selisih.toDouble() / bulanLalu.toDouble() * 100).toInt()
        return if (persen >= 0) "+$persen% dari bulan lalu" else "$persen% dari bulan lalu"
    }

    fun isNaik(bulanIni: Long, bulanLalu: Long): Boolean {
        return bulanIni > bulanLalu
    }
}
