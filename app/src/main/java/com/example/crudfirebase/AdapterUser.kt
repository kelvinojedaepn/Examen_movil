package com.example.crudfirebase

import android.content.Intent
import android.util.Log
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
import org.w3c.dom.Text


class AdapterUser(var userList: ArrayList<User>, private val contenidoIntentExplicito: ActivityResultLauncher<Intent>) : RecyclerView.Adapter<AdapterUser.MyViewHolderUser>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderUser {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)

        return MyViewHolderUser(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolderUser, position: Int) {
        val currentItem = userList[position]
        holder.idUser.text = currentItem.id
        holder.firstName.text = currentItem.fistName
        holder.lastName.text = currentItem.lastName
        holder.age.text = currentItem.age.toString()
        holder.btnEdit.setOnClickListener {
            abrirActividadConParametros(EditUserInfoActivity::class.java, currentItem, it)
        }
        holder.isMen.text = currentItem.isMen.toString()
        holder.salary.text = currentItem.salary.toString()
        holder.maritateStatus.text = currentItem.maritateStatus

        holder.btnDelete.setOnClickListener {

            val builder = AlertDialog.Builder(it.context)
            builder.setTitle("Confirm Deletion")
            builder.setMessage("Are you sure you want to delete?")
            builder.setPositiveButton("Yes") { dialog, _ ->
                // Delete the item
                dialog.dismiss()
                // Perform the deletion here
                deleteUser(currentItem.id!!)
                this.userList.remove(currentItem)
                notifyDataSetChanged()
            }
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
        holder.btnViewCars.setOnClickListener {
            abrirActividadConParametros(CarListActivity::class.java, currentItem, it)
        }
    }

    private fun deleteUser(id: String) {
        val db = Firebase.firestore
        val users = db.collection("users")
        val userDoc = users.document(id)
        userDoc.delete().addOnSuccessListener {

        }.addOnFailureListener {

        }


    }

    private fun abrirActividadConParametros(clase: Class<*>, user: User, it: View?) {
        val intent = Intent(it!!.context, clase)
        intent.putExtra("userId", user!!.id)
        intent.putExtra("userFirstName", user!!.fistName)
        intent.putExtra("userLastName", user!!.lastName)
        intent.putExtra("userAge", user!!.age)
        intent.putExtra("userSalary", user!!.salary)
        intent.putExtra("userIsMen", user!!.isMen)
        intent.putExtra("userMaritateStatus", user!!.maritateStatus)
        contenidoIntentExplicito.launch(intent)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    

    class MyViewHolderUser(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val firstName = itemView.findViewById<TextView>(R.id.tvFirstName)
        val lastName = itemView.findViewById<TextView>(R.id.tvLastName)
        val age = itemView.findViewById<TextView>(R.id.tvAge)
        val btnEdit = itemView.findViewById<Button>(R.id.btn_edit)
        val btnDelete = itemView.findViewById<Button>(R.id.btn_delete)
        val btnViewCars = itemView.findViewById<Button>(R.id.btn_view_cars)
        val idUser = itemView.findViewById<TextView>(R.id.tvIdUser)
        val isMen = itemView.findViewById<TextView>(R.id.tvIsMen)
        val salary = itemView.findViewById<TextView>(R.id.tvSalary)
        val maritateStatus = itemView.findViewById<TextView>(R.id.tvMaritate)
    }


}