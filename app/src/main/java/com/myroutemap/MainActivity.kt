package com.myroutemap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    //se llama automaticamente cuando el mapa se haya cargado
    override fun onMapReady(map: GoogleMap) {
        this.map = map
    }

    fun getRetrofit(): Retrofit {
        return  Retrofit.Builder()
            .baseUrl("https://api.openrouteservice.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

// https://api.openrouteservice.org/v2/
// directions/driving-car?api_key=5b3ce3597851110001cf6248b568bcde1a2445dc93decd70c099178b&start=8.681495,49.41461&end=8.687872,49.420318

    }
}

