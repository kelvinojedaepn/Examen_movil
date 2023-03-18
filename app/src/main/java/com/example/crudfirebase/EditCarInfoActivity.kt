package com.example.crudfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class EditCarInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_car_info)
        var id = intent.getStringExtra("userId")
        var carId = intent.getStringExtra("carId")
        var carName = intent.getStringExtra("carName")
        var carMileage = intent.getDoubleExtra("mileage", 0.0)
        var carDoorNumber = intent.getIntExtra("doorNumber", 0)
        var carIsNew = intent.getBooleanExtra("isNew", true)
        var carLicense = intent.getStringExtra("carLicense")
        var beginLicense = intent.getStringExtra("beginLicense")


        var textName = this.findViewById<EditText>(R.id.tit_branch)
        textName.setText(carName)
        var textDoorNumber = this.findViewById<EditText>(R.id.tit_doors)
        textDoorNumber.setText(carDoorNumber.toString())
        var textMileage = this.findViewById<EditText>(R.id.tit_mileage)
        textMileage.setText(carMileage.toString())
        var textIsNoNewOption = this.findViewById<RadioButton>(R.id.option_isNew_no)
        var textIsYesNewOption = this.findViewById<RadioButton>(R.id.option_isNew_yes)

        if(carLicense == null){
            carLicense = ""
        }
        if(beginLicense == null){
            beginLicense = ""
        }
        var textCarLicense = this.findViewById<EditText>(R.id.tit_license)
        textCarLicense.setText(carLicense)
        var textBeginCarLicense = this.findViewById<EditText>(R.id.tit_begin_license_edit)
        textBeginCarLicense.setText(beginLicense)

        var textIsNew: Boolean = true

        val selectIsNewStatus = this.findViewById<RadioGroup>(R.id.rg_is_new_car)
        if(carIsNew == false){
            selectIsNewStatus.check(textIsNoNewOption.id)
        }else{
            selectIsNewStatus.check(textIsYesNewOption.id)
        }

        selectIsNewStatus.setOnCheckedChangeListener { group, checkedId ->
            textIsNew = checkedId != textIsNoNewOption.id
        }



        val btnSaveCar = this.findViewById<Button>(R.id.btn_save_car)
        btnSaveCar.setOnClickListener {
            if (!checkChange(
                    carName,
                    carMileage,
                    carDoorNumber,
                    carIsNew,
                    carLicense,
                    beginLicense,
                    textName.text.toString(),
                    textMileage.text.toString(),
                    textDoorNumber.text.toString(),
                    textIsNew,
                    textCarLicense.text.toString(),
                    textBeginCarLicense.text.toString()

                )
            ) {

                if (carId != null) {
                    saveData(
                        id!!,
                        carId!!,
                        textName.text.toString(),
                        textMileage.text.toString().toDouble(),
                        textDoorNumber.text.toString().toInt(),
                        textIsNew,
                        textCarLicense.text.toString(),
                        textBeginCarLicense.text.toString()
                    )
                } else {
                    carId = Date().time.toString()
                    saveData(
                        id!!,
                        carId!!,
                        textName.text.toString(),
                        textMileage.text.toString().toDouble(),
                        textDoorNumber.text.toString().toInt(),
                        textIsNew,
                        textCarLicense.text.toString(),
                        textBeginCarLicense.text.toString()
                    )
                }
            }
            abrirActividadConParametros(CarListActivity::class.java, it, id!!)

        }


    }

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

    private fun abrirActividadConParametros(clase: Class<*>, it: View?, idOwner: String) {
        val intent = Intent(it!!.context, clase)
        intent.putExtra("userId", idOwner)
        contenidoIntentExplicito.launch(intent)
    }

    private fun saveData(
        id: String,
        carId: String,
        textName: String,
        textMileage: Double,
        textDoorNumber: Int,
        textIsNew: Boolean,
        textCarLicense: String,
        textBeginCarLicense: String
    ) {
        val db = Firebase.firestore
        val users = db.collection("users")
        val carsCollectionsRef = users.document(id!!).collection("cars")
        val data3Car = hashMapOf(
            "id" to carId,
            "name" to textName,
            "doorNumber" to textDoorNumber,
            "mileage" to textMileage,
            "isNew" to textIsNew,
            "carLicense" to textCarLicense,
            "beginLicense" to textBeginCarLicense
        )
        carsCollectionsRef.document(carId).set(data3Car)
    }

    private fun checkChange(
        carName: String?,
        carMileage: Double,
        carDoorNumber: Int,
        carIsNew: Boolean,
        carLicense: String?,
        beginLicense: String?,
        textName: String,
        textMileage: String,
        textDoorNumber: String,
        textIsNew: Boolean,
        textCarLicense: String,
        textBeginCarLicense: String
    ): Boolean {

        return (carName == textName && carMileage == textMileage.toDouble() && carDoorNumber == textDoorNumber.toInt() && carIsNew == textIsNew  && carLicense == textCarLicense && beginLicense == textBeginCarLicense)
    }


    private fun irActividad(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }
}