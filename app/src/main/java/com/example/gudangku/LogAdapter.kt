package com.example.gudangku

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.core.graphics.toColorInt
import java.text.SimpleDateFormat
import java.util.*

class LogAdapter(private val listLog: List<RiwayatBarang>) :
    RecyclerView.Adapter<LogAdapter.LogViewHolder>() {

    class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvJudul: TextView = itemView.findViewById(R.id.tv_judul_log)
        val tvDesc: TextView = itemView.findViewById(R.id.tv_desc_log)
        val tvWaktu: TextView = itemView.findViewById(R.id.tv_waktu_log)
        val imgIcon: ImageView = itemView.findViewById(R.id.img_icon_log)
        val cardIcon: CardView = itemView.findViewById(R.id.card_icon_bg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_log, parent, false)
        return LogViewHolder(view)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val item = listLog[position]

        holder.tvJudul.text = when (item.jenis) {
            "MASUK" -> "Barang Masuk"
            "KELUAR" -> "Barang Keluar"
            "EDIT" -> "Edit Barang"
            "HAPUS" -> "Hapus Barang"

            else -> "Aktivitas"
        }

        holder.tvDesc.text = when (item.jenis) {
            "MASUK" -> "Gudang: ${item.namaGudang}" +
                        "\nOleh: ${item.namaUser}" +
                        "\n+${item.jumlah} ${item.namaBarang}"

            "KELUAR" -> "Gudang: ${item.namaGudang}" +
                        "\nOleh: ${item.namaUser}" +
                        "\n+${item.jumlah} ${item.namaBarang}"

            "EDIT" -> "Gudang: ${item.namaGudang}" +
                        "\nOleh: ${item.namaUser}" +
                        "\nNama Barang: ${item.namaBarang}" +
                        "\nCatatan: ${item.catatan}"

            "HAPUS" -> "Gudang: ${item.namaGudang}" +
                        "\nOleh: ${item.namaUser}" +
                        "\n${item.namaBarang} dihapus"

            else ->
                item.namaBarang
        }

        holder.tvWaktu.text = formatWaktu(item.tanggal)

        when (item.jenis) {
            "MASUK" -> setIcon(holder, "#E8F5E9", "#2E7D32", R.drawable.ic_add)
            "KELUAR" -> setIcon(holder, "#FFEBEE", "#C62828", R.drawable.ic_delete)
            "EDIT" -> setIcon(holder, "#E3F2FD", "#1565C0", R.drawable.ic_ganti)
            "HAPUS" -> setIcon(holder, "#FDECEA", "#B71C1C", R.drawable.ic_delete)
        }
    }

    override fun getItemCount() = listLog.size

    private fun setIcon(holder: LogViewHolder, bg: String, iconColor: String, iconRes: Int) {
        holder.cardIcon.setCardBackgroundColor(bg.toColorInt())
        holder.imgIcon.setColorFilter(iconColor.toColorInt())
        holder.imgIcon.setImageResource(iconRes)
    }

    private fun formatWaktu(time: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(time))
    }
}