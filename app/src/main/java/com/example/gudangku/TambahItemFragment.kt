package com.example.gudangku

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class TambahItemFragment : Fragment() {

    private lateinit var etJumlah: EditText
    private lateinit var etKode: EditText
    private lateinit var layoutDynamic: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tambah_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etJumlah = view.findViewById(R.id.et_jumlah_produk)
        etKode = view.findViewById(R.id.et_kode_item)
        layoutDynamic = view.findViewById(R.id.layout_dynamic_inputs)

        view.findViewById<SwitchCompat>(R.id.switch_kode_manual).setOnCheckedChangeListener { _, isChecked ->
            etKode.isEnabled = isChecked
            etKode.alpha = if (isChecked) 1.0f else 0.5f
        }

        view.findViewById<ImageView>(R.id.btn_qty_up).setOnClickListener {
            val current = etJumlah.text.toString().toIntOrNull() ?: 0
            etJumlah.setText((current + 1).toString())
        }

        view.findViewById<ImageView>(R.id.btn_qty_down).setOnClickListener {
            val current = etJumlah.text.toString().toIntOrNull() ?: 0
            if (current > 0) etJumlah.setText((current - 1).toString())
        }

        view.findViewById<TextView>(R.id.btn_add_description).setOnClickListener {
            val newEt = EditText(requireContext()) // Pakai requireContext()
            newEt.hint = "Deskripsi Tambahan"
            newEt.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_input_field)
            newEt.setPadding(30, 30, 30, 30)
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 150)
            params.setMargins(0, 0, 0, 20)
            newEt.layoutParams = params
            layoutDynamic.addView(newEt)
        }

        view.findViewById<Button>(R.id.btn_simpan_persediaan).setOnClickListener {
            Toast.makeText(requireContext(), "Item Berhasil Ditambahkan!", Toast.LENGTH_SHORT).show()
        }
    }
}