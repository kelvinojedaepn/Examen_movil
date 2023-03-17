package com.example.crudfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class EditUserInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user_info)
        var id = intent.getStringExtra("userId")
        val firstName = intent.getStringExtra("userFirstName")
        val lastName = intent.getStringExtra("userLastName")
        val age = intent.getIntExtra("userAge", 0)
        val salary = intent.getDoubleExtra("userSalary", 0.0)
        val isMen = intent.getBooleanExtra("userIsMen", true)
        val maritateStatus = intent.getStringExtra("userMaritateStatus")

        var textFistName = this.findViewById<EditText>(R.id.tit_first_name)
        var textLastName = this.findViewById<EditText>(R.id.tit_last_name)
        var textAge = this.findViewById<EditText>(R.id.tit_age)
        var textSalary = this.findViewById<EditText>(R.id.tit_salary)
        var selectIsMen = this.findViewById<RadioGroup>(R.id.rg_is_men)
        if (isMen == false) {
            selectIsMen.check(R.id.option_woman)
        } else {
            selectIsMen.check(R.id.option_man)
        }


        var selectMaritateStatus = this.findViewById<RadioGroup>(R.id.rg_maritate_status)
        if (maritateStatus == "M") {
            selectMaritateStatus.check(R.id.option_m)
        } else {
            selectMaritateStatus.check(R.id.option_s)
        }
        textFistName.setText(firstName)
        textLastName.setText(lastName)
        textAge.setText(age.toString())
        textSalary.setText(salary.toString())
        var isMenOp: Boolean = true
        val selectedRadioButtonMen = selectIsMen!!.checkedRadioButtonId
        if (selectedRadioButtonMen == R.id.option_man) {
            isMenOp = false
        }
        val selectedRadioButtonMaritate = selectMaritateStatus!!.checkedRadioButtonId
        var textMaritateStatus = "S"
        if (selectedRadioButtonMaritate == R.id.option_m){
            textMaritateStatus = "M"
        }

        val btnSaveData = this.findViewById<Button>(R.id.btn_save)
        btnSaveData.setOnClickListener {
            if (!checkChanges(
                    firstName,
                    lastName,
                    age,
                    salary,
                    isMen,
                    maritateStatus,
                    textFistName.text.toString(),
                    textLastName.text.toString(),
                    textAge.text.toString().toInt(),
                    textSalary.text.toString().toDouble(),
                    isMenOp,
                    textMaritateStatus
                )
            ) {

                if (id != null) {
                    saveData(
                        id!!,
                        textFistName.text.toString(),
                        textLastName.text.toString(),
                        textAge.text.toString().toInt(),
                        textSalary.text.toString().toDouble(),
                        isMenOp,
                        textMaritateStatus
                    )
                } else {
                    id = Date().time.toString()
                    saveData(
                        id!!,
                        textFistName.text.toString(),
                        textLastName.text.toString(),
                        textAge.text.toString().toInt(),
                        textSalary.text.toString().toDouble(),
                        isMenOp,
                        textMaritateStatus
                    )
                }
            }
            irActividad(UserListActivity::class.java)
        }


    }

    private fun checkChanges(
        firstName: String?,
        lastName: String?,
        age: Int,
        salary: Double,
        men: Boolean,
        maritateStatus: String?,
        textFistName: String,
        textLastName: String,
        textAge: Int,
        textSalary: Double,
        selectIsMen: Boolean?,
        selectMaritateStatus: String?
    ): Boolean {



        return (firstName == textFistName && lastName == textLastName && age == textAge && salary == textSalary && men == selectIsMen && maritateStatus == selectMaritateStatus)

    }


    private fun saveData(
        id: String,
        firstName: String,
        lastName: String,
        age: Int,
        salary: Double,
        isMen: Boolean,
        maritateStatus: String
    ) {
        val db = Firebase.firestore
        val users = db.collection("users")

        val data1 = hashMapOf(
            "id" to id,
            "fistName" to firstName,
            "lastName" to lastName,
            "age" to age,
            "salary" to salary,
            "isMen" to isMen,
            "maritateStatus" to maritateStatus
        )
        users.document(id).set(data1)
    }

    private fun irActividad(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)
        startActivity(intent)
    }


}