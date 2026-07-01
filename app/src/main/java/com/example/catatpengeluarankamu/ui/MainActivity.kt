package com.example.catatpengeluarankamu.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.catatpengeluarankamu.R
import com.example.catatpengeluarankamu.databinding.ActivityMainBinding
import com.example.catatpengeluarankamu.ui.dashboard.DashboardFragment
import com.example.catatpengeluarankamu.ui.pengaturan.PengaturanFragment
import com.example.catatpengeluarankamu.ui.riwayat.RiwayatFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tampilkan Dashboard saat pertama buka
        if (savedInstanceState == null) {
            loadFragment(DashboardFragment())
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> loadFragment(DashboardFragment())
                R.id.nav_riwayat -> loadFragment(RiwayatFragment())
                R.id.nav_pengaturan -> loadFragment(PengaturanFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
