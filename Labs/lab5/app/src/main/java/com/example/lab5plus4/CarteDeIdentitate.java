package com.example.lab5plus4;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.Serializable;

public class CarteDeIdentitate implements Parcelable, Serializable {
    private static final long serialVersionUID = 1L;
    private String nume;
    private int varsta;
    private boolean esteCasatorit;
    private double inaltime;
    private TipSex sex;
    private Date dataEmitere;


    // Constructor
    public CarteDeIdentitate(String nume, int varsta, boolean esteCasatorit,
                             double inaltime, TipSex sex, Date dataEmitere) {
        this.nume = nume;
        this.varsta = varsta;
        this.esteCasatorit = esteCasatorit;
        this.inaltime = inaltime;
        this.sex = sex;
        this.dataEmitere = dataEmitere;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getVarsta() {
        return varsta;
    }

    public void setVarsta(int varsta) {
        this.varsta = varsta;
    }

    public boolean isEsteCasatorit() {
        return esteCasatorit;
    }

    public void setEsteCasatorit(boolean esteCasatorit) {
        this.esteCasatorit = esteCasatorit;
    }

    public double getInaltime() {
        return inaltime;
    }

    public void setInaltime(double inaltime) {
        this.inaltime = inaltime;
    }

    public TipSex getSex() {
        return sex;
    }

    public void setSex(TipSex sex) {
        this.sex = sex;
    }

    public Date getDataEmitere() {
        return dataEmitere;
    }

    public void setDataEmitere(Date dataEmitere) {
        this.dataEmitere = dataEmitere;
    }

    // ToString personalizat pentru afișare
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dataFormatata = sdf.format(dataEmitere);

        return "***CarteDeIdentitate***\n" +
                "Nume: " + nume + '\n' +
                "Vârstă: " + varsta + '\n' +
                "Căsătorit: " + (esteCasatorit ? "Da" : "Nu") + '\n' +
                "Înălțime: " + inaltime + " m\n" +
                "Sex: " + sex + '\n' +
                "Data emiterii: " + dataFormatata + '\n';
    }

    // --- Parcelable implementation ---

    protected CarteDeIdentitate(Parcel in) {
        nume = in.readString();
        varsta = in.readInt();
        esteCasatorit = in.readByte() != 0;
        inaltime = in.readDouble();
        sex = TipSex.valueOf(in.readString());
        dataEmitere = new Date(in.readLong());
    }

    public static final Creator<CarteDeIdentitate> CREATOR = new Creator<CarteDeIdentitate>() {
        @Override
        public CarteDeIdentitate createFromParcel(Parcel in) {
            return new CarteDeIdentitate(in);
        }

        @Override
        public CarteDeIdentitate[] newArray(int size) {
            return new CarteDeIdentitate[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nume);
        dest.writeInt(varsta);
        dest.writeByte((byte) (esteCasatorit ? 1 : 0));
        dest.writeDouble(inaltime);
        dest.writeString(sex.name());
        dest.writeLong(dataEmitere.getTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
