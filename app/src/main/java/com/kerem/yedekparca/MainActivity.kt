package com.kerem.yedekparca

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var sharedPreferences = this.getSharedPreferences("database_kayit" , Context.MODE_PRIVATE)

        /*Araba Modelleri Database Oluşturma*/
        val arabaCsv = InputStreamReader(assets.open("arabalar.csv"))
        var reader = BufferedReader(arabaCsv)
        val arabaCol : ArrayList<String> = reader.readLine().split(";") as ArrayList<String>
        arabaCol[0] = arabaCol[0].split(arabaCol[0][0])[1]
        try
        {
            val database = this.openOrCreateDatabase("YedekParcalar" , Context.MODE_PRIVATE, null)
            database.execSQL("CREATE TABLE IF NOT EXISTS  Arabalar  (id INTEGER PRIMARY KEY , ${arabaCol[0]} VARCHAR , ${arabaCol[1]} VARCHAR ,${arabaCol[2]} VARCHAR , ${arabaCol[3]} INTEGER)")

            if(sharedPreferences.getInt("ilk" ,0) == 0) {
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    val temp = line?.split(";")
                    database.execSQL("INSERT INTO Arabalar (${arabaCol[0]},${arabaCol[1]},${arabaCol[2]},${arabaCol[3]}) VALUES ('${temp?.get(0)}' , '${temp?.get(1)}' , '${temp?.get(2)}' , ${temp?.get(3)?.toInt()})")
                }
            }

            reader.close()
        }
        catch (e : Exception){
            e.printStackTrace()
            println("Araba Database Okunurken Hata Oldu")
        }

        /*Yedek parçaları databeseye ekleme*/
        val yedekParcaCsv = InputStreamReader(assets.open("yedek_parcalar.csv"))
        reader = BufferedReader(yedekParcaCsv)
        val yedekParcaCol : ArrayList<String> = reader.readLine().split(";") as ArrayList<String>
        yedekParcaCol[0] = yedekParcaCol[0].split(yedekParcaCol[0][0])[1]
        try
        {
            val database = this.openOrCreateDatabase("YedekParcalar" , Context.MODE_PRIVATE, null)
            database.execSQL("CREATE TABLE IF NOT EXISTS  YedekParcalar  (id INTEGER PRIMARY KEY , ${yedekParcaCol[0]} VARCHAR , ${yedekParcaCol[1]} VARCHAR ,${yedekParcaCol[2]} VARCHAR , ${yedekParcaCol[3]} VARCHAR , ${yedekParcaCol[4]} INTEGER)")
            if(sharedPreferences.getInt("ilk" ,0) == 0) {
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    val temp = line?.split(";")
                    database.execSQL("INSERT INTO YedekParcalar (${yedekParcaCol[0]},${yedekParcaCol[1]},${yedekParcaCol[2]},${yedekParcaCol[3]} ,${yedekParcaCol[4]}) VALUES ('${temp?.get(0)}' , '${temp?.get(1)}' , '${temp?.get(2)}' ,'${temp?.get(3)}' , ${temp?.get(4)?.toInt()})")
                }
            }

            reader.close()
        }
        catch (e : Exception){
            e.printStackTrace()
            println("YedekParcalar Database Okunurken Hata Oldu")
        }



        /*n-n database*/
        val eslesmeCsv = InputStreamReader(assets.open("eslesme.csv"))
        reader = BufferedReader(eslesmeCsv)
        val EslesmeCol : ArrayList<String> = reader.readLine().split(";") as ArrayList<String>
        EslesmeCol[0] = EslesmeCol[0].split(EslesmeCol[0][0])[1]
        try
        {
            val database = this.openOrCreateDatabase("YedekParcalar" , Context.MODE_PRIVATE, null)
            database.execSQL("CREATE TABLE IF NOT EXISTS  Eslesmeler  (id INTEGER PRIMARY KEY , ${EslesmeCol[0]} INTEGER , ${EslesmeCol[1]} INTEGER)")
            if(sharedPreferences.getInt("ilk" ,0) == 0) {
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    val temp = line?.split(";")
                    database.execSQL("INSERT INTO Eslesmeler (${EslesmeCol[0]},${EslesmeCol[1]}) VALUES ('${temp?.get(0)?.toInt()}' , '${temp?.get(1)?.toInt()}')")
                }
            }

            reader.close()
        }
        catch (e : Exception){
            e.printStackTrace()
            println("Eslesmeler Database Okunurken Hata Oldu")
        }

        sharedPreferences.edit().putInt("ilk" , 1).apply()
    }

   fun araba_tanimla(view: View){
       val intent = Intent(this , ArabaModeliTanimla::class.java)
       startActivity(intent)
    }

    fun yedekParcaEkle(view: View)
    {
        val intent = Intent(this , KayitEkrani::class.java)
        startActivity(intent)
    }

    fun parca_uyumlu_arac_listesi(view : View)
    {
        val intent = Intent(this , ParcaUyumluArabaListesi::class.java)
        startActivity(intent)
    }

    fun aracEkle(view: View)
    {
        val intent = Intent(this , AracEkle::class.java)
        startActivity(intent)
    }

    fun stokSorgula(view: View)
    {
        val intent = Intent(this , StokSorgulama::class.java)
        startActivity(intent)
    }

    fun parcaSil(view: View)
    {
        val intent = Intent(this , ParcaSil::class.java)
        startActivity(intent)
    }


}