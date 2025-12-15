package com.example.gudangku

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RiwayatAdapter(
    private val context: Context,
    private val listItem: List<ItemRiwayat>
) : RecyclerView.Adapter<RiwayatAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama: TextView = view.findViewById(R.id.tv_nama_riwayat)
        val tvKode: TextView = view.findViewById(R.id.tv_kode_riwayat)
        val tvStok: TextView = view.findViewById(R.id.tv_stok_riwayat)
        val tvWaktu: TextView = view.findViewById(R.id.tv_waktu_masuk_keluar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_riwayat, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItem[position]

        holder.tvNama.text = item.nama
        holder.tvKode.text = "Kode: ${item.kode}"
        holder.tvStok.text = "${item.jumlah} ${item.satuan}"
        holder.tvWaktu.text = item.waktu

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DeskripsiItemActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = listItem.size
}