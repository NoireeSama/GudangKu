package com.example.gudangku

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PersediaanFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_persediaan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rv = view.findViewById<RecyclerView>(R.id.rv_persediaan)

        val listBarang = mutableListOf(
            Barang("Beras Bulog 5Kg", "R1", "5kg", 13, "karung"),
            Barang("Tanggo Kaleng", "R1", "350g", 32, "Box"),
            Barang("Haruna Biscuit", "R2", "200g", 0, "Box")
        )

        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = PersediaanAdapter(requireContext(), listBarang)
    }
}