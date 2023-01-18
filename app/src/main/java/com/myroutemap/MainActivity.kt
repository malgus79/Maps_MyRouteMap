package com.myroutemap

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var btnCalculate: Button

    private var start: String = ""
    private var end: String = ""

    var poly: Polyline? = null

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCalculate = findViewById(R.id.btnCalculateRoute)
        btnCalculate.setOnClickListener {

            start = ""
            end = ""

            poly?.remove()
            poly = null  //para borrar la ruta anterior
            Toast.makeText(this, "Selecciona punto de origen y destino", Toast.LENGTH_SHORT).show()

            //siempre comprobar primero si el mapa está cargado
            if (::map.isInitialized) {
                map.setOnMapClickListener {
                    if (start.isEmpty()) {
                        //la API recibe el orden LONG/LATI, no al reves
                        start = "${it.longitude},${it.latitude}"
                    } else if (end.isEmpty()) {
                        end = "${it.longitude},${it.latitude}"
                        createRoute()
                    }
                }
            }
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    //se llama automaticamente cuando el mapa se haya cargado
    override fun onMapReady(map: GoogleMap) {
        this.map = map
    }

    //metodo para hacer la llamada a la interface de retrofit
    private fun createRoute() {

/*
        //para saber que vienen en esas 2 variables
        Log.i("SSSTATUS", start)
        Log.i("SSSTATUS", end)
*/

        CoroutineScope(Dispatchers.IO).launch {
            //llamada al servidor
            val call = getRetrofit().create(ApiService::class.java)
                .getRoute("5b3ce3597851110001cf6248b568bcde1a2445dc93decd70c099178b", start, end)
            if (call.isSuccessful) {
//                Log.i("SSSTATUS", "OK")
                drawRoute(call.body())                
            } else {
                Log.i("SSSTATUS", "KOOOOO")
            }
        }
    }

    private fun drawRoute(routeResponse: RouteResponse?) {
        val polylineOptions = PolylineOptions()
        routeResponse?.features?.first()?.geometry?.coordinates?.forEach {
            polylineOptions.add(LatLng(it[1], it[0]))
        }
        runOnUiThread {
           poly = map.addPolyline(polylineOptions)
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openrouteservice.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

// https://api.openrouteservice.org/v2/
// directions/driving-car?api_key=5b3ce3597851110001cf6248b568bcde1a2445dc93decd70c099178b&start=8.681495,49.41461&end=8.687872,49.420318

    }
}

