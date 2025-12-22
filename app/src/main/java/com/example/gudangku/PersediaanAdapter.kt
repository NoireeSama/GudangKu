package com.example.gudangku

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*

class PersediaanAdapter(
    private val context: Context,
    private val listBarang: MutableList<PersediaanDetail>
) : RecyclerView.Adapter<PersediaanAdapter.PersediaanViewHolder>() {

    private val db = GudangKuDatabase.getInstance(context)

    class PersediaanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNama: TextView = itemView.findViewById(R.id.tv_nama_barang)
        val tvJumlah: TextView = itemView.findViewById(R.id.tv_jumlah)
        val tvSatuan: TextView = itemView.findViewById(R.id.tv_satuan)
        val btnPlus: ImageView = itemView.findViewById(R.id.btn_plus)
        val btnMinus: ImageView = itemView.findViewById(R.id.btn_minus)
        val cardRoot: CardView = itemView.findViewById(R.id.card_root)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersediaanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_persediaan, parent, false)
        return PersediaanViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersediaanViewHolder, position: Int) {
        val item = listBarang[position]

        holder.tvNama.text = item.namaBarang
        holder.tvJumlah.text = item.stok.toString()
        holder.tvSatuan.text = item.satuan

        val colorResId = when {
            item.stok == 0 -> R.color.alert_red
            item.stok <= 10 -> R.color.warning_orange
            else -> R.color.primary_blue
        }
        holder.cardRoot.setCardBackgroundColor(
            ContextCompat.getColor(context, colorResId)
        )

        // ✅ KLIK DETAIL
        holder.cardRoot.setOnClickListener {
            val intent = Intent(context, DeskripsiItemActivity::class.java)
            intent.putExtra("ID_PERSEDIAAN", item.idPersediaan)
            context.startActivity(intent)
        }

        // ➕ SIMPAN KE DB
        holder.btnPlus.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.persediaanDao().updateStok(
                    item.idPersediaan,
                    item.stok + 1
                )
            }
        }

        holder.btnMinus.setOnClickListener {
            if (item.stok > 0) {
                CoroutineScope(Dispatchers.IO).launch {
                    db.persediaanDao().updateStok(
                        item.idPersediaan,
                        item.stok - 1
                    )
                }
            }
        }
    }

    override fun getItemCount() = listBarang.size
}
