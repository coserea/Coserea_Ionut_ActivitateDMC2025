package com.example.cosereaionutc114a;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HusaTelefon implements Parcelable{
    private String material;
    private int lungime;
    private boolean customizable;
    private CeCuloare color;

    public HusaTelefon(String material, int lungime, boolean customizable, CeCuloare color){
        this.color = color;
        this.material = material;
        this.customizable = customizable;
        this.lungime = lungime;
    }

    public String getMaterial() {
        return material;
    }

    public int getLungime(){
        return lungime;
    }

    public boolean getCustomizable(){
        return customizable;
    }

    public CeCuloare getCuloare(){
        return color;
    }

    public void setMaterial(String material){
        this.material = material;
    }

    public void setLungime(int lungime){
        this.lungime = lungime;
    }

    public void setCustomizable(boolean customizable){
        this.customizable = customizable;
    }

    public void setColor(CeCuloare color){
        this.color = color;
    }

    // ToString personalizat pentru afișare
    @Override
    public String toString() {

        return "***Husa***\n" +
                "Material: " + material + '\n' +
                "Lungime: " + lungime + '\n' +
                "Customizable?: " + (customizable ? "Da" : "Nu") + '\n' +
                "Color: " + color + "\n";
    }

    // --- Parcelable implementation ---

    protected HusaTelefon(Parcel in) {
        material = in.readString();
        lungime = in.readInt();
        customizable = in.readByte() != 0;
        color = CeCuloare.valueOf(in.readString());
    }

    public static final Parcelable.Creator<HusaTelefon> CREATOR = new Parcelable.Creator<HusaTelefon>() {
        @Override
        public HusaTelefon createFromParcel(Parcel in) {
            return new HusaTelefon(in);
        }

        @Override
        public HusaTelefon[] newArray(int size) {
            return new HusaTelefon[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(material);
        dest.writeInt(lungime);
        dest.writeByte((byte) (customizable ? 1 : 0));
        dest.writeString(color.name());
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
