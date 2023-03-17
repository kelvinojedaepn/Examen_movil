package com.example.crudfirebase

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdapterCar(
    var carList: ArrayList<Car>,
    private val contenidoIntentExplicito: ActivityResultLauncher<Intent>,
    val idOwner: String
) : RecyclerView.Adapter<AdapterCar.MyViewHolderCar>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderCar {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.car_item, parent, false)
        return MyViewHolderCar(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolderCar, position: Int) {
        val currentCar = carList[position]
        holder.idCar.text = currentCar.id
        holder.nameCar.text = currentCar.name
        holder.doorNumber.text = currentCar.doorNumber.toString()
        holder.mileage.text = currentCar.mileage.toString()
        holder.isNew.text = currentCar.isNew.toString()
        holder.carLicense.text = currentCar.carLicense
        holder.beginLicense.text = currentCar.beginLicense
        holder.btnEditCar.setOnClickListener {
            abrirActividadConParametros(EditCarInfoActivity::class.java, currentCar, it, idOwner)
        }
        holder.btnDeleteCar.setOnClickListener {

            val builder = AlertDialog.Builder(it.context)
            builder.setTitle("Confirm Deletion")
            builder.setMessage("Are you sure you want to delete?")
            builder.setPositiveButton("Yes") { dialog, _ ->
                // Delete the item
                dialog.dismiss()
                // Perform the deletion here
                deleteCar(currentCar.id!!, idOwner)
                this.carList.remove(currentCar)
                notifyDataSetChanged()
            }
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()

        }

    }

    private fun deleteCar(id: String, idOwner: String) {
        val db = Firebase.firestore
        val users = db.collection("users")
        val carsCollectionsRef = users.document(idOwner).collection("cars")
        carsCollectionsRef.document(id).delete().addOnSuccessListener {

        }.addOnFailureListener {

        }



    }

    override fun getItemCount(): Int {
        return this.carList.size
    }

    private fun abrirActividadConParametros(clase: Class<*>, car: Car, it: View?, idOwner: String) {
        val intent = Intent(it!!.context, clase)
        intent.putExtra("carId", car!!.id)
        intent.putExtra("carName", car!!.name)
        intent.putExtra("userId", idOwner)
        intent.putExtra("doorNumber", car!!.doorNumber)
        intent.putExtra("mileage", car!!.mileage)
        intent.putExtra("isNew", car!!.isNew)
        intent.putExtra("carLicense", car!!.carLicense)
        intent.putExtra("beginLicense", car!!.beginLicense)
        contenidoIntentExplicito.launch(intent)
    }

    class MyViewHolderCar(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val idCar = itemView.findViewById<TextView>(R.id.tvIdCar)
        val nameCar = itemView.findViewById<TextView>(R.id.tvNameCar)
        val btnDeleteCar = itemView.findViewById<Button>(R.id.btn_delete_car)
        val btnEditCar = itemView.findViewById<Button>(R.id.btn_edit_car)
        val doorNumber = itemView.findViewById<TextView>(R.id.tv_door_number)
        val mileage = itemView.findViewById<TextView>(R.id.tv_mileage_car)
        val isNew = itemView.findViewById<TextView>(R.id.tv_is_new)
        val carLicense = itemView.findViewById<TextView>(R.id.tv_license)
        val beginLicense = itemView.findViewById<TextView>(R.id.tv_begin_license)

    }


}