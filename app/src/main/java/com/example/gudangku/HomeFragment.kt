package com.example.gudangku

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
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

        // VIEW
        val tvUsername = view.findViewById<TextView>(R.id.tv_username)
        val tvNamaGudang = view.findViewById<TextView>(R.id.tv_nama_gudang)
        val tvAlamatGudang = view.findViewById<TextView>(R.id.tv_alamat_gudang)

        val tvBarangMasuk = view.findViewById<TextView>(R.id.tv_barang_masuk)
        val tvBarangKeluar = view.findViewById<TextView>(R.id.tv_barang_keluar)
        val tvTotalBarang = view.findViewById<TextView>(R.id.tv_total_barang)
        val tvTotalBerat = view.findViewById<TextView>(R.id.tv_total_berat)

        val rvBarang = view.findViewById<RecyclerView>(R.id.rv_barang)
        rvBarang.layoutManager = LinearLayoutManager(requireContext())
        val tvInfo = view.findViewById<TextView>(R.id.tv_stock_info)

        tvUsername.text = session.getDisplayName()

        // LOAD DATA
        loadHomeData(
            tvNamaGudang,
            tvAlamatGudang,
            tvBarangMasuk,
            tvBarangKeluar,
            tvTotalBarang,
            tvTotalBerat,
            rvBarang,
            tvInfo
        )

        // NAV
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
        view.findViewById<ImageView>(R.id.btn_search).setOnClickListener {
            val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
            bottomNav.selectedItemId = R.id.nav_persediaan
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
                it.findViewById(R.id.rv_barang),
                it.findViewById(R.id.tv_stock_info)
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
        rvBarang: RecyclerView,
        tvInfo: TextView
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

            db.persediaanDao()
                .getPersediaanByGudang(idGudang)
                .collect { listBarang ->

                    val stokKritis = listBarang
                        .filter { it.stok <= 10}

                    withContext(Dispatchers.Main) {
                        tvNamaGudang.text = gudang.namaGudang
                        tvAlamatGudang.text = "${gudang.lokasiGudang}\n${gudang.kodeGudang}"

                        tvBarangMasuk.text = summary.barangMasuk.toString()
                        tvBarangKeluar.text = summary.barangKeluar.toString()
                        tvTotalBarang.text = summary.totalBarang.toString()

                        val beratGram = summary.totalBerat ?: 0.0
                        tvTotalBerat.text = if (beratGram < 1000) {
                                                String.format("%.0f Gram", beratGram)
                                            } else {
                                                String.format("%.2f Kg", beratGram / 1000)
                                            }

                        if (stokKritis.isEmpty()) {
                            tvInfo.visibility = View.VISIBLE
                            tvInfo.text = "Semua stok aman"
                            rvBarang.adapter = PersediaanAdapter(
                                requireContext(),
                                mutableListOf()
                            )
                        } else {
                            tvInfo.visibility = View.GONE
                            rvBarang.adapter = PersediaanAdapter(
                                requireContext(),
                                stokKritis.toMutableList()
                            )
                        }
                    }
                }

        }
    }
}