package com.example.gudangku

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.collect

class HomeFragment : Fragment() {

    private lateinit var session: SessionManager
    private lateinit var db: GudangKuDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        session = SessionManager(requireContext())
        db = GudangKuDatabase.getInstance(requireContext())

        if (!session.isLoggedIn()) {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
            return
        }

        // ================= VIEW =================
        val tvUsername = view.findViewById<TextView>(R.id.tv_username)
        val tvNamaGudang = view.findViewById<TextView>(R.id.tv_nama_gudang)
        val tvAlamatGudang = view.findViewById<TextView>(R.id.tv_alamat_gudang)

        val tvBarangMasuk = view.findViewById<TextView>(R.id.tv_barang_masuk)
        val tvBarangKeluar = view.findViewById<TextView>(R.id.tv_barang_keluar)
        val tvTotalBarang = view.findViewById<TextView>(R.id.tv_total_barang)
        val tvTotalBerat = view.findViewById<TextView>(R.id.tv_total_berat)

        val rvBarang = view.findViewById<RecyclerView>(R.id.rv_barang)
        rvBarang.layoutManager = LinearLayoutManager(requireContext())

        tvUsername.text = session.getDisplayName()

        // ================= LOAD DATA =================
        loadHomeData(
            tvNamaGudang,
            tvAlamatGudang,
            tvBarangMasuk,
            tvBarangKeluar,
            tvTotalBarang,
            tvTotalBerat,
            rvBarang
        )

        // ================= NAV =================
        view.findViewById<View>(R.id.btnHalamanUser).setOnClickListener {
            startActivity(Intent(requireContext(), ProfileActivity::class.java)
            )
        }
        view.findViewById<ImageView>(R.id.btn_notif).setOnClickListener {
            startActivity(Intent(requireContext(), NotifikasiActivity::class.java)
            )
        }
        view.findViewById<ImageView>(R.id.btn_ganti_gudang).setOnClickListener {
            startActivity(Intent(requireContext(), DaftarGudangActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        view?.let {
            loadHomeData(
                it.findViewById(R.id.tv_nama_gudang),
                it.findViewById(R.id.tv_alamat_gudang),
                it.findViewById(R.id.tv_barang_masuk),
                it.findViewById(R.id.tv_barang_keluar),
                it.findViewById(R.id.tv_total_barang),
                it.findViewById(R.id.tv_total_berat),
                it.findViewById(R.id.rv_barang)
            )
        }
    }

    private fun loadHomeData(
        tvNamaGudang: TextView,
        tvAlamatGudang: TextView,
        tvBarangMasuk: TextView,
        tvBarangKeluar: TextView,
        tvTotalBarang: TextView,
        tvTotalBerat: TextView,
        rvBarang: RecyclerView
    ) {
        lifecycleScope.launch {
            val idGudang = session.getGudangAktifId()

            val gudang = db.gudangDao().getById(idGudang)
            val summary = db.persediaanDao().getHomeSummary(idGudang)

            if (gudang == null) {
                withContext(Dispatchers.Main) {
                    tvNamaGudang.text = "Belum ada gudang"
                    tvAlamatGudang.text = "Silakan pilih gudang"
                }
                return@launch
            }

            // 🔥 COLLECT FLOW
            db.persediaanDao()
                .getPersediaanByGudang(idGudang)
                .collect { listBarang ->

                    withContext(Dispatchers.Main) {
                        tvNamaGudang.text = gudang.namaGudang
                        tvAlamatGudang.text =
                            "${gudang.lokasiGudang}\n${gudang.kodeGudang}"

                        tvBarangMasuk.text = summary.barangMasuk.toString()
                        tvBarangKeluar.text = summary.barangKeluar.toString()
                        tvTotalBarang.text = summary.totalBarang.toString()
                        tvTotalBerat.text =
                            String.format("%.2f Gram", summary.totalBerat ?: 0.0)

                        rvBarang.adapter = PersediaanAdapter(
                            requireContext(),
                            listBarang.toMutableList() // ✅ AMAN
                        )
                    }
                }
        }

    }

    private fun showEmptyState(
        tvNamaGudang: TextView,
        tvAlamatGudang: TextView,
        tvBarangMasuk: TextView,
        tvBarangKeluar: TextView,
        tvTotalBarang: TextView,
        tvTotalBerat: TextView,
        rvBarang: RecyclerView
    ) {
        tvNamaGudang.text = "Belum ada gudang"
        tvAlamatGudang.text = "Silakan pilih gudang"

        tvBarangMasuk.text = "0"
        tvBarangKeluar.text = "0"
        tvTotalBarang.text = "0"
        tvTotalBerat.text = "0 Kg"

        rvBarang.adapter = PersediaanAdapter(requireContext(), mutableListOf())
    }
}
