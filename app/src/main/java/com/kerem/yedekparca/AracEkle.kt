package com.kerem.yedekparca

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_arac_ekle.*
import kotlinx.android.synthetic.main.activity_kayit_ekrani.*

class AracEkle : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arac_ekle)

    }

    fun kontrol(arac_marka : String , arac_model : String , kasa_tipi : String , uretim_yili : String) : Boolean
    {
        if(arac_marka == "")
        {
            Toast.makeText(applicationContext, "Marka kısmı boş geçilemez", Toast.LENGTH_LONG).show()
            return false
        }

        if(arac_model == "")
        {
            Toast.makeText(applicationContext, "Model kısmı boş geçilemez", Toast.LENGTH_LONG).show()
            return false
        }

        if(kasa_tipi == "")
        {
            Toast.makeText(applicationContext, "Kasa tipi kısmı boş geçilemez", Toast.LENGTH_LONG).show()
            return false
        }

        if(uretim_yili == "")
        {
            Toast.makeText(applicationContext, "Üretim yılı kısmı boş geçilemez", Toast.LENGTH_LONG).show()
            return false
        }

        else
        {
            if (uretim_yili.toInt() < 1950 || uretim_yili.toInt() > 2023)
            {
                Toast.makeText(applicationContext, "Girilen yıllar istenilen aralıkta değildir(1950-2023)", Toast.LENGTH_LONG).show()
                return false
            }
        }

        return true
    }

    fun db_control(database : SQLiteDatabase , arac_marka : String , arac_model : String , kasa_tipi : String , uretim_yili : String) : Boolean
    {
        val cursor = database.rawQuery("SELECT * FROM Arabalar WHERE Marka LIKE '${arac_marka}' AND Model lIKE '${arac_model}' AND Kasa_Tipi lIKE '${kasa_tipi}' AND Uretim_Yili = ${uretim_yili.toInt()}" , null)

        var flag = false
        while(cursor.moveToNext())
        {
            flag = true
        }

        cursor.close()
        return flag
    }

    fun db_ekle(database : SQLiteDatabase , arac_marka : String , arac_model : String , kasa_tipi : String , uretim_yili : String)
    {
        database.execSQL("INSERT INTO Arabalar (Marka,Model,Kasa_Tipi,Uretim_Yili) VALUES ('${arac_marka}' , '${arac_model}' , '${kasa_tipi}' , ${uretim_yili.toInt()})")
    }

    fun ekle(view : View)
    {
        val arac_marka = marka.text.toString()
        val arac_model = model.text.toString()
        val kasa_tipi = kasaTipi.text.toString()
        val uretim_yili = uretimYili.text.toString()

        if(kontrol(arac_marka , arac_model , kasa_tipi , uretim_yili) == false)
        {
            finish()
            return
        }

        val database = this.openOrCreateDatabase("YedekParcalar" , Context.MODE_PRIVATE, null)
        if(db_control(database, arac_marka, arac_model, kasa_tipi, uretim_yili) == true)
        {
            Toast.makeText(applicationContext, "Araç Zaten Sistemde Mevcuttur!", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        db_ekle(database, arac_marka, arac_model, kasa_tipi, uretim_yili)
        finish()
    }
}