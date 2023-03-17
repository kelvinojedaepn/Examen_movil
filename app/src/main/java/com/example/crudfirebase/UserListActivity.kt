package com.example.crudfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class UserListActivity : AppCompatActivity() {


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


    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userArrayList: ArrayList<User>
    private lateinit var adaptador: AdapterUser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        userRecyclerView = findViewById(R.id.userListRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.setHasFixedSize(true)
        userArrayList = arrayListOf<User>()
        adaptador = AdapterUser(userArrayList, contenidoIntentExplicito)
        userRecyclerView.adapter = adaptador

        consultarDocumentos()

        val botonAnadirDatos = this.findViewById<Button>(R.id.btn_add_data)
        botonAnadirDatos.setOnClickListener{
            crearDatosPrueba()
        }

        val btnLeerDatos = this.findViewById<Button>(R.id.btn_read_data)
        btnLeerDatos.setOnClickListener{
            consultarDocumentos()
        }

        val btnCreateData = this.findViewById<Button>(R.id.btn_create_data)
         btnCreateData.setOnClickListener {
             irActividad(EditUserInfoActivity::class.java)
         }






    }

    private fun crearDatosPrueba() {
        val db = Firebase.firestore
        val users = db.collection("users")
        var identification = Date().time


        val data3 = hashMapOf(
            "id" to identification.toString(),
            "fistName" to "Juan123",
            "lastName" to "Ortega123",
            "age" to 14
        )
        users.document(identification.toString()).set(data3)

        var identificationCar = Date().time
        val carsCollectionsRef = users.document(identification.toString()).collection("cars")
        val data3Car = hashMapOf(
            "id" to identificationCar.toString(),
            "name" to "nissan"
        )
        carsCollectionsRef.document(identificationCar.toString()).set(data3Car)
        identificationCar = Date().time
        val data3Car1 = hashMapOf(
            "id" to identificationCar.toString(),
            "name" to "toyota"
        )
        carsCollectionsRef.document(identificationCar.toString()).set(data3Car1)


    }

    private fun consultarDocumento(){
        val db = Firebase.firestore
        val users = db.collection("users")
        users.document("1234567890").get().addOnSuccessListener {
            limpiarArreglo()
            this.userArrayList.add(
                User(
                    it.data?.get("id") as String?,
                    it.data?.get("fistName") as String?,
                    it.data?.get("lastName") as String?,
                    (it.data?.get("age") as Number?)?.toInt()
                )
            )
        }
        for(user in userArrayList){
            print(user.toString())
        }
        adaptador.userList = userArrayList
        adaptador.notifyDataSetChanged()

    }


    private fun consultarDocumentos(){
        val db = Firebase.firestore
        val usersRef = db.collection("users")
        limpiarArreglo()
        usersRef.get().addOnSuccessListener { result ->
            for (document in result) {
                val user = User(
                    document.get("id") as String?,
                    document.get("fistName") as String?,
                    document.get("lastName") as String?,
                    (document.get("age") as Number?)?.toInt(),
                    (document.get("salary") as Number?)?.toDouble(),
                    document.get("isMen") as Boolean?,
                    document.get("maritateStatus") as String?

                )
                userArrayList.add(user)
            }
            // Actualizar la lista de usuarios en la vista
            adaptador.userList = userArrayList
            adaptador.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
            Log.d(null, "Error al obtener usuarios", exception)
        }
    }



    private fun limpiarArreglo() {
        this.userArrayList.clear()
    }

    private fun irActividad(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }





}