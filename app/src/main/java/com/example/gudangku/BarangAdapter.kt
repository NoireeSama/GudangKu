package com.example.gudangku

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class BarangAdapter(private val context: Context, private val listBarang: List<Barang>) :
    RecyclerView.Adapter<BarangAdapter.BarangViewHolder>() {

    class BarangViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tv_nama_barang)
        val tvDetail: TextView = itemView.findViewById(R.id.tv_detail)
        val tvJumlah: TextView = itemView.findViewById(R.id.tv_jumlah)
        val tvSatuan: TextView = itemView.findViewById(R.id.tv_satuan)
        val cardRoot: CardView = itemView.findViewById(R.id.card_root)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarangViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_barang, parent, false)
        return BarangViewHolder(view)
    }

    override fun onBindViewHolder(holder: BarangViewHolder, position: Int) {
        val item = listBarang[position]

        holder.tvNama.text = item.nama
        holder.tvDetail.text = "Rak: ${item.rak}\nBerat: ${item.berat}"
        holder.tvJumlah.text = item.stok.toString()
        holder.tvSatuan.text = item.satuan

        val colorResId = when {
            item.stok == 0 -> R.color.alert_red
            item.stok <= 10 -> R.color.warning_orange
            else -> R.color.primary_blue
        }
        holder.cardRoot.setCardBackgroundColor(ContextCompat.getColor(context, colorResId))

        holder.cardRoot.setOnClickListener {
            val intent = Intent(context, DeskripsiItemActivity::class.java)
            intent.putExtra("ID_BARANG", item.idBarang)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listBarang.size
}