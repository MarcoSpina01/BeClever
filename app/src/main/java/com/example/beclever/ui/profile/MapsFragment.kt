package com.example.beclever.ui.profile

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.beclever.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

/**
 * Fragment per la visualizzazione di una mappa Google Maps.
 *
 * Questo fragment visualizza una mappa Google Maps centrata su Ancona e aggiunge un marker per rappresentare
 * la posizione di Ancona sulla mappa.
 */
class MapsFragment : Fragment() {

    // Callback per l'inizializzazione della mappa
    private val callback = OnMapReadyCallback { googleMap ->

        // Coordinate geografiche di Ancona
        val ancona = LatLng(43.5867868, 13.5161161)

        // Aggiungi un marker per Ancona con un titolo
        googleMap.addMarker(MarkerOptions().position(ancona).title("Marker a Ancona"))

        // Muovi la camera della mappa per centrarla su Ancona con uno zoom specifico
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ancona, 17f))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Trova il fragment della mappa nell'interfaccia utente e assegna la callback per l'inizializzazione
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}