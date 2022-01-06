package com.kerem.yedekparca

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_kayit_ekrani.*
import kotlinx.android.synthetic.main.activity_parca_sil.*

class ParcaSil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parca_sil)
    }

    fun parca_sil(view: View)
    {
        val parca_adi = parca_adi_yedek.text.toString()
        val parca_markasi = parca_marka.text.toString()


        if(parca_adi == "" && parca_markasi == "")
        {
            Toast.makeText(applicationContext, "Parça Adı Ve Parça Markası kısmı boş geçilemez", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        if(parca_adi == "")
        {
            Toast.makeText(applicationContext, "Parça Adı kısmı boş geçilemez", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        if(parca_markasi == "")
        {
            Toast.makeText(applicationContext, "Parça markası kısmı boş geçilemez", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        var database = this.openOrCreateDatabase("YedekParcalar" , Context.MODE_PRIVATE, null)

        var flag2 = false
        var cursor2 = database.rawQuery("SELECT * FROM YedekParcalar WHERE Yedek_Parca_Adi LIKE '${parca_adi}'" , null)
        while(cursor2.moveToNext())
        {
            flag2 = true
        }



        var flag3 = false
        var cursor3 = database.rawQuery("SELECT * FROM YedekParcalar WHERE Yedek_Parca_Markasi LIKE '${parca_markasi}'" , null)
        while(cursor3.moveToNext())
        {
            flag3 = true
        }

        if(flag2 == false)
        {
            Toast.makeText(applicationContext, "Girdiğiniz parça adı sistemde bulunmamaktadır", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        if(flag3 == false)
        {
            Toast.makeText(applicationContext, "Girdiğiniz markada parça bulunmamaktadır", Toast.LENGTH_LONG).show()
            finish()
            return
        }


        var cursor = database.rawQuery("SELECT * FROM YedekParcalar WHERE Yedek_Parca_Adi LIKE '${parca_adi}' AND Yedek_Parca_Markasi LIKE '${parca_markasi}'" , null)
        val id = cursor.getColumnIndex("id")

        var parca_id = 0
        var flag = false
        while(cursor.moveToNext())
        {
            parca_id = cursor.getString(id).toInt()
            flag = true
        }

        if(flag == true) {
            database.execSQL("DELETE FROM YedekParcalar WHERE id = ${parca_id}")
            database.execSQL("DELETE FROM Eslesmeler WHERE ID_PARCA = ${parca_id}")
        }
    }

}