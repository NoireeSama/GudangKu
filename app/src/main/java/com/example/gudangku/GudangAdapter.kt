package com.example.gudangku

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class GudangAdapter(
    private val context: Context
) : RecyclerView.Adapter<GudangAdapter.GudangViewHolder>() {

    private var listGudang = listOf<TableGudang>()

    inner class GudangViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tv_nama_gudang)
        val tvAlamat: TextView = itemView.findViewById(R.id.tv_alamat)
        val btnInfo: ImageView = itemView.findViewById(R.id.btn_info)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GudangViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gudang, parent, false)
        return GudangViewHolder(view)
    }

    override fun onBindViewHolder(holder: GudangViewHolder, position: Int) {
        val item = listGudang[position]

        holder.tvNama.text = item.namaGudang
        holder.tvAlamat.text =
            "${item.lokasiGudang}\n${item.kodeGudang}"

        // ✅ PILIH GUDANG
        holder.itemView.setOnClickListener {
            val session = SessionManager(context)
            session.setActiveGudang(
                item.idGudang,
                item.namaGudang,
                item.kodeGudang
            )

            Toast.makeText(
                context,
                "Gudang ${item.namaGudang} dipilih",
                Toast.LENGTH_SHORT
            ).show()

            // 🔥 KUNCI UTAMA
            if (context is DaftarGudangActivity) {
                context.finish()
            }
        }

        // INFO BUTTON
        holder.btnInfo.setOnClickListener {
            val intent = Intent(context, DeskripsiGudangActivity::class.java)
            intent.putExtra("ID_GUDANG", item.idGudang)
            context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int = listGudang.size

    fun updateData(newList: List<TableGudang>) {
        listGudang = newList
        notifyDataSetChanged()
    }
}
