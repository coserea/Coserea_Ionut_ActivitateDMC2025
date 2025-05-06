package com.example.lab4

import android.os.Parcel
import android.os.Parcelable

enum class GenCarte {
    FICTIUNE, NON_FICTIUNE, SF, ROMANTIC, ISTORIE, BIOGRAFIE
}

data class Carte(
    val titlu: String,
    val autor: String,
    val anPublicatie: Int,
    val pagini: Int,
    val esteEbook: Boolean,
    val gen: GenCarte
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        GenCarte.valueOf(parcel.readString() ?: "FICTIUNE")
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(titlu)
        parcel.writeString(autor)
        parcel.writeInt(anPublicatie)
        parcel.writeInt(pagini)
        parcel.writeByte(if (esteEbook) 1 else 0)
        parcel.writeString(gen.name)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Carte> {
        override fun createFromParcel(parcel: Parcel): Carte {
            return Carte(parcel)
        }

        override fun newArray(size: Int): Array<Carte?> {
            return arrayOfNulls(size)
        }
    }
}
