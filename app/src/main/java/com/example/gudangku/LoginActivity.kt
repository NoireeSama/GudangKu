package com.example.gudangku

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.core.graphics.toColorInt

class LogAdapter(private val listLog: List<LogNotifikasi>) :
    RecyclerView.Adapter<LogAdapter.LogViewHolder>() {

    class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvJudul: TextView = itemView.findViewById(R.id.tv_judul_log)
        val tvDesc: TextView = itemView.findViewById(R.id.tv_desc_log)
        val tvWaktu: TextView = itemView.findViewById(R.id.tv_waktu_log)
        val imgIcon: ImageView = itemView.findViewById(R.id.img_icon_log)
        val cardIcon: CardView = itemView.findViewById(R.id.card_icon_bg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_log, parent, false)
        return LogViewHolder(view)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val item = listLog[position]

        holder.tvJudul.text = item.judul
        holder.tvDesc.text = item.deskripsi
        holder.tvWaktu.text = item.waktu

        when (item.tipe) {
            "MASUK" -> {
                holder.cardIcon.setCardBackgroundColor("#E8F5E9".toColorInt())
                holder.imgIcon.setColorFilter("#2E7D32".toColorInt())
                holder.imgIcon.setImageResource(R.drawable.ic_add)
            }
            "KELUAR" -> {
                holder.cardIcon.setCardBackgroundColor("#FFEBEE".toColorInt())
                holder.imgIcon.setColorFilter("#C62828".toColorInt())
                holder.imgIcon.setImageResource(R.drawable.ic_launcher_foreground)
                holder.imgIcon.rotation = 90f
            }
            "EDIT" -> {
                // Oranye / Biru
                holder.cardIcon.setCardBackgroundColor("#E3F2FD".toColorInt())
                holder.imgIcon.setColorFilter("#1565C0".toColorInt())
                holder.imgIcon.setImageResource(R.drawable.ic_launcher_foreground)
                holder.imgIcon.rotation = 0f
            }
        }
    }

    override fun getItemCount() = listLog.size
}