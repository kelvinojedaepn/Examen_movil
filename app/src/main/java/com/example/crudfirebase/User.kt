package com.example.crudfirebase

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import java.net.IDN

data class User(
    val id: String?=null,
    var fistName: String? = null,
    var lastName: String? = null,
    var age: Int? = 0,
    var salary: Double? = 0.0,
    var isMen: Boolean? = true,
    var maritateStatus: String? = "S"
    ): Parcelable{
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel): this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readBoolean(),
        parcel.readString()
    )

    override fun describeContents(): Int {
        return 0
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(fistName)
        parcel.writeString(lastName)
        age?.let { parcel.writeInt(it) }
        salary?.let { parcel.writeDouble(it) }
        isMen?.let { parcel.writeBoolean(it) }
        parcel.writeString(maritateStatus)
    }

    companion object CREATOR : Parcelable.Creator<User> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
