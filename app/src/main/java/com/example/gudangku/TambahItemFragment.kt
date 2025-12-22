package com.example.gudangku

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TambahItemFragment : Fragment() {

    private lateinit var etJumlah: EditText
    private lateinit var etKode: EditText
    private lateinit var layoutDynamic: LinearLayout
    private lateinit var etNama: EditText
    private lateinit var etBerat: EditText
    private lateinit var etJenis: EditText
    private lateinit var switchJumlah: SwitchCompat

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_tambah_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etJumlah = view.findViewById(R.id.et_jumlah_produk)
        etKode = view.findViewById(R.id.et_kode_item)
        layoutDynamic = view.findViewById(R.id.layout_dynamic_inputs)
        etNama = view.findViewById(R.id.et_nama_item)
        etBerat = view.findViewById(R.id.et_berat_item)
        etJenis = view.findViewById(R.id.et_jenis_item)
        switchJumlah = view.findViewById(R.id.switch_jumlah_manual)

        // 🔁 Switch kode manual
        view.findViewById<SwitchCompat>(R.id.switch_kode_manual)
            .setOnCheckedChangeListener { _, isChecked ->
                etKode.isEnabled = isChecked
                etKode.alpha = if (isChecked) 1f else 0.5f
            }

        // 🔁 Switch jumlah manual
        switchJumlah.setOnCheckedChangeListener { _, isChecked ->
            etJumlah.isEnabled = isChecked
            etJumlah.alpha = if (isChecked) 1f else 0.5f
        }

        // ➕ qty
        view.findViewById<ImageView>(R.id.btn_qty_up).setOnClickListener {
            if (!switchJumlah.isChecked) {
                val current = etJumlah.text.toString().toIntOrNull() ?: 0
                etJumlah.setText((current + 1).toString())
            }
        }

        // ➖ qty
        view.findViewById<ImageView>(R.id.btn_qty_down).setOnClickListener {
            if (!switchJumlah.isChecked) {
                val current = etJumlah.text.toString().toIntOrNull() ?: 0
                if (current > 0) etJumlah.setText((current - 1).toString())
            }
        }

        // ➕ tambah deskripsi
        view.findViewById<TextView>(R.id.btn_add_description).setOnClickListener {
            val et = EditText(requireContext())
            et.hint = "Deskripsi Tambahan"
            et.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.bg_input_field)
            et.setPadding(30, 30, 30, 30)

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 20)
            et.layoutParams = params

            layoutDynamic.addView(et)
        }

        // 💾 SIMPAN
        view.findViewById<Button>(R.id.btn_simpan_persediaan).setOnClickListener {

            val nama = etNama.text.toString().trim()
            val jenis = etJenis.text.toString().trim()
            val berat = etBerat.text.toString().toFloatOrNull() ?: 0f
            val jumlah = etJumlah.text.toString().toIntOrNull() ?: 0
            val kode =
                if (etKode.isEnabled && etKode.text.isNotEmpty())
                    etKode.text.toString()
                else
                    "BRG-${System.currentTimeMillis()}"

            if (nama.isEmpty() || jumlah <= 0) {
                Toast.makeText(requireContext(), "Data belum lengkap", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val session = SessionManager(requireContext())
            val gudangId = session.getGudangAktifId()

            if (gudangId == -1) {
                Toast.makeText(requireContext(), "Gudang belum dipilih", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val deskripsiGabungan = (0 until layoutDynamic.childCount)
                .mapNotNull { index ->
                    (layoutDynamic.getChildAt(index) as? EditText)
                        ?.text?.toString()?.takeIf { it.isNotBlank() }
                }
                .joinToString("\n")

            val db = GudangKuDatabase.getInstance(requireContext())

            lifecycleScope.launch(Dispatchers.IO) {

                val barangId = db.barangDao().insert(
                    TableBarang(
                        kodeBarang = kode,
                        namaBarang = nama,
                        jenisBarang = jenis,
                        beratBarang = berat,
                        deskripsiBarang = deskripsiGabungan,
                        satuanBarang = "pcs"
                    )
                ).toInt()

                db.persediaanDao().insert(
                    TablePersediaan(
                        idBarang = barangId,
                        idGudang = gudangId,
                        stok = jumlah
                    )
                )

                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Item berhasil ditambahkan",
                        Toast.LENGTH_SHORT
                    ).show()
                    parentFragmentManager.popBackStack()
                }
            }
        }
    }
}
