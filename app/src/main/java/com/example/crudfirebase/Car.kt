package com.example.crudfirebase

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

data class Car(
    val id: String? = null,
    var name: String? = null,
    var doorNumber: Int?=0,
    var mileage: Double?= 0.0,
    var isNew: Boolean? = true,
    var carLicense: String? = "",
    var beginLicense: String? = ""

):Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel):this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readBoolean(),
        parcel.readString(),
        parcel.readString()
    )

    override fun describeContents(): Int {
        return 0
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        doorNumber?.let { parcel.writeInt(it) }
        mileage?.let { parcel.writeDouble(it) }
        isNew?.let { parcel.writeBoolean(it) }
        parcel.writeString(carLicense)
        parcel.writeString(beginLicense)
    }
    companion object CREATOR : Parcelable.Creator<Car> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): Car {
            return Car(parcel)
        }

        override fun newArray(size: Int): Array<Car?> {
            return arrayOfNulls(size)
        }
    }
}