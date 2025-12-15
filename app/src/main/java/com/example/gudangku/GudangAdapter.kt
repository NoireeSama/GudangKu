package com.example.gudangku

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Gudang(val nama: String, val alamat: String)

class GudangAdapter(private val context: Context, private val listGudang: List<Gudang>) :
    RecyclerView.Adapter<GudangAdapter.GudangViewHolder>() {

    class GudangViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tv_nama_gudang)
        val tvAlamat: TextView = itemView.findViewById(R.id.tv_alamat)
        val btnInfo: ImageView = itemView.findViewById(R.id.btn_info)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GudangViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gudang, parent, false)
        return GudangViewHolder(view)
    }

    override fun onBindViewHolder(holder: GudangViewHolder, position: Int) {
        val item = listGudang[position]
        holder.tvNama.text = item.nama
        holder.tvAlamat.text = item.alamat

        holder.btnInfo.setOnClickListener {
            val intent = Intent(context, DeskripsiGudangActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = listGudang.size
}