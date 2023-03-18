package com.example.crudfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class CarListActivity : AppCompatActivity() {

    private val contenidoIntentExplicito = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            if (result.data != null) {
                val data = result.data
                Log.i("Intente-epn", "${data?.getStringExtra("nombreModificado")}")
            }
        }
    }

    private lateinit var carRecyclerView: RecyclerView
    private lateinit var carArrayList: ArrayList<Car>
    private lateinit var adapterCar: AdapterCar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_list)

        var id = intent.getStringExtra("userId")

        carRecyclerView = findViewById(R.id.carListRecyclerView)
        carRecyclerView.layoutManager = LinearLayoutManager(this)
        carRecyclerView.setHasFixedSize(true)
        carArrayList = arrayListOf<Car>()
        if (id != null) {
            consultarDocumentosCarros(id!!)
        }
        adapterCar = AdapterCar(carArrayList, contenidoIntentExplicito, id!!)
        carRecyclerView.adapter = adapterCar

        val btnNewCar = findViewById<Button>(R.id.btn_new_car)
        btnNewCar.setOnClickListener {
            abrirActividadConParametros(EditCarInfoActivity::class.java, it, id)
        }
    }

    private fun consultarDocumentosCarros(id: String) {

        val db = Firebase.firestore
        val userRef = db.collection("users").document(id)
        val carsCollectionRef = userRef.collection("cars")
        limpiarArreglo()
        carsCollectionRef.get().addOnSuccessListener { querySnaps ->
            for (document in querySnaps.documents) {
                val car = Car(
                    document.get("id") as String?,
                    document.get("name") as String?,
                    (document.get("doorNumber") as Number?)?.toInt(),
                    (document.get("mileage") as Number?)?.toDouble(),
                    document.get("isNew") as Boolean?,
                    document.get("carLicense") as String?,
                    document.get("beginLicense") as String?
                )
                    this.carArrayList.add(car)

            }
            adapterCar.carList = carArrayList
            adapterCar.notifyDataSetChanged()
        }

    }

    private fun limpiarArreglo() {
        this.carArrayList.clear()
    }

    private fun abrirActividadConParametros(clase: Class<*>, it: View?, idOwner: String) {
        val intent = Intent(it!!.context, clase)
        intent.putExtra("userId", idOwner)
        contenidoIntentExplicito.launch(intent)
    }
}