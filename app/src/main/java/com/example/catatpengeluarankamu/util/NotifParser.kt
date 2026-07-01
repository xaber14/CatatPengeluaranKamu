package com.example.catatpengeluarankamu.util

object NotifParser {

    // Package name resmi masing-masing app
    val SUPPORTED_PACKAGES = mapOf(
        "com.bca" to "myBCA",
        "com.gojek.app" to "GoPay",
        "ovo.id" to "OVO"
    )

    data class HasilParsing(
        val nominal: Long,
        val kategori: String,
        val perluDicek: Boolean
    )

    // Kata kunci yang menandakan ini transaksi PENGELUARAN (bukan pemasukan/OTP/promo)
    private val keywordPengeluaran = listOf(
        "pembayaran", "transaksi", "berhasil", "debit", "qris",
        "transfer", "bayar", "belanja", "pembelian", "payment"
    )

    // Kata kunci yang menandakan ini BUKAN pengeluaran (diabaikan)
    private val keywordAbaikan = listOf(
        "otp", "kode verifikasi", "masuk", "kredit", "diterima",
        "promo", "cashback", "reward", "top up berhasil", "saldo masuk"
    )

    fun parse(packageName: String, judul: String, isi: String): HasilParsing? {
        val teksGabungan = "$judul $isi".lowercase()

        // Cek apakah ini notifikasi yang perlu diabaikan
        if (keywordAbaikan.any { teksGabungan.contains(it) }) return null

        // Cek apakah ada keyword pengeluaran
        val adaKeywordPengeluaran = keywordPengeluaran.any { teksGabungan.contains(it) }

        // Ambil nominal dari teks
        val nominal = extractNominal(teksGabungan) ?: return null

        // Tentukan kategori otomatis
        val kategori = tentukanKategori(teksGabungan)

        // Tandai perlu dicek jika: tidak ada keyword jelas ATAU kategori tidak terdeteksi
        val perluDicek = !adaKeywordPengeluaran || kategori == "Lainnya"

        return HasilParsing(
            nominal = nominal,
            kategori = kategori,
            perluDicek = perluDicek
        )
    }

    private fun extractNominal(teks: String): Long? {
        // Pola: Rp50.000 / Rp 50.000 / Rp50,000 / IDR 50000
        val pola = Regex("""(?:rp\.?\s*|idr\s*)(\d{1,3}(?:[.,]\d{3})*(?:[.,]\d{2})?)""")
        val hasil = pola.find(teks) ?: return null

        return try {
            // Bersihkan titik dan koma pemisah ribuan, ambil angkanya saja
            val angkaBersih = hasil.groupValues[1]
                .replace(".", "")
                .replace(",", "")
            angkaBersih.toLong()
        } catch (e: NumberFormatException) {
            null
        }
    }

    private fun tentukanKategori(teks: String): String {
        return when {
            teks.containsAny("indomaret", "alfamart", "alfamidi", "lawson", "minimarket", "supermarket", "hypermart", "giant", "carrefour", "belanja") -> "Belanja"
            teks.containsAny("gojek", "grab", "ojek", "taxi", "taksi", "busway", "transjakarta", "kereta", "commuter", "mrt", "lrt", "parkir", "bensin", "pertamina", "shell", "transport") -> "Transport"
            teks.containsAny("resto", "restoran", "restaurant", "kfc", "mcdonald", "burger", "pizza", "sushi", "cafe", "kopi", "coffee", "makan", "food", "kuliner", "warung", "indomie") -> "Makan & Minum"
            teks.containsAny("listrik", "pln", "air", "pdam", "internet", "wifi", "telkom", "indihome", "tagihan", "token", "pulsa", "paket data") -> "Tagihan"
            teks.containsAny("bioskop", "cinema", "cgv", "xxi", "netflix", "spotify", "game", "hiburan", "tiket", "konser") -> "Hiburan"
            teks.containsAny("apotik", "apotek", "klinik", "rumah sakit", "dokter", "obat", "kesehatan") -> "Kesehatan"
            else -> "Lainnya"
        }
    }

    private fun String.containsAny(vararg keywords: String): Boolean {
        return keywords.any { this.contains(it) }
    }
}
