package com.example.gudangku

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class PersediaanFragment : Fragment() {

    private lateinit var adapter: PersediaanAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_persediaan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rv = view.findViewById<RecyclerView>(R.id.rv_persediaan)
        rv.layoutManager = LinearLayoutManager(requireContext())

        adapter = PersediaanAdapter(
            requireContext(),
            mutableListOf()
        )
        rv.adapter = adapter

        val session = SessionManager(requireContext())
        val db = GudangKuDatabase.getInstance(requireContext())
        val idGudang = session.getGudangAktifId()

        if (idGudang == -1) return

        lifecycleScope.launch {
            db.persediaanDao()
                .getPersediaanByGudang(idGudang)
                .collect { listBarang ->
                    adapter.updateData(listBarang)
                }
        }
    }
}
