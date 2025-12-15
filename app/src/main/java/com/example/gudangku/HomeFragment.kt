package com.example.gudangku

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {

    // Deklarasi SessionManager (Opsional ditaruh global, tapi di dalam onViewCreated lebih aman untuk Fragment)
    // private lateinit var session: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ==========================================
        // 1. INISIALISASI SESSION MANAGER
        // ==========================================
        val session = SessionManager(requireContext())

        // ==========================================
        // 2. LOGIC PROFILE (Username & Klik)
        // ==========================================
        val tvUsername = view.findViewById<TextView>(R.id.tv_username)
        val imgProfile = view.findViewById<ImageView>(R.id.img_profile) // Tambahan biar gambar juga bisa diklik

        // Set nama user dari Session (menggantikan "Aditya Permana" static di XML)
        tvUsername.text = session.getUsername()

        // Bikin listener biar bisa dipake di teks maupun gambar
        val openProfileListener = View.OnClickListener {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(intent)
        }

        // Pasang listener klik
        tvUsername.setOnClickListener(openProfileListener)
        imgProfile.setOnClickListener(openProfileListener)


        // ==========================================
        // 3. LOGIC RECYCLERVIEW (Kode Lamamu)
        // ==========================================
        val rvBarang = view.findViewById<RecyclerView>(R.id.rv_barang)
        val listBarang = listOf(
            Barang("Beras Bulog 5Kg", "R1L1-1", "5kg/item", 13, "karung"),
            Barang("Tanggo Kaleng", "R1L1-2", "350g/item", 32, "Box"),
            Barang("Haruna Biscuit", "R2L2-5", "200g/item", 0, "Box")
        )

        rvBarang.layoutManager = LinearLayoutManager(requireContext())
        rvBarang.adapter = BarangAdapter(requireContext(), listBarang)


        // ==========================================
        // 4. LOGIC TOMBOL LAINNYA (Kode Lamamu)
        // ==========================================
        view.findViewById<ImageView>(R.id.btn_ganti_gudang)?.setOnClickListener {
            startActivity(Intent(requireContext(), DaftarGudangActivity::class.java))
        }

        view.findViewById<ImageView>(R.id.btn_notif)?.setOnClickListener {
            startActivity(Intent(requireContext(), NotifikasiActivity::class.java))
        }

        view.findViewById<View>(R.id.card_barang_masuk)?.setOnClickListener {
            startActivity(Intent(requireContext(), BarangMasukActivity::class.java))
        }

        view.findViewById<View>(R.id.card_barang_keluar)?.setOnClickListener {
            startActivity(Intent(requireContext(), BarangKeluarActivity::class.java))
        }
    }
}