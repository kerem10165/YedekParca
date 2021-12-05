package com.kerem.yedekparca

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_kayit_ekrani.*
import kotlinx.android.synthetic.main.activity_stok_sorgulama.*

class StokSorgulama : AppCompatActivity() {
    lateinit var database : SQLiteDatabase
    var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stok_sorgulama)
        database = this.openOrCreateDatabase("YedekParcalar" , Context.MODE_PRIVATE, null)
    }

    fun kontrol(parca_adi : String , parca_markasi : String) : Boolean
    {
        if(parca_adi == "")
        {
            Toast.makeText(applicationContext, "Parça Adı kısmı boş geçilemez", Toast.LENGTH_LONG).show()
            return false
        }

        if(parca_markasi == "")
        {
            Toast.makeText(applicationContext, "Parça Markası kısmı boş geçilemez", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    fun db_kontrol(database: SQLiteDatabase , parca_adi : String , parca_markasi : String , parca_kategorisi : String) : Boolean
    {
        //str = "AND Yedek_Parca_Kategorisi LIKE '${newText}%'"
        var str = "SELECT * FROM YedekParcalar WHERE Yedek_Parca_Adi LIKE '${parca_adi}' AND Yedek_Parca_Markasi LIKE '${parca_markasi}'"
        if(parca_kategorisi != "")
        {
            str+= " AND Yedek_Parca_Kategorisi LIKE '${parca_kategorisi}'"
        }

        var cursor = database.rawQuery(str , null)

        val id_cursor = cursor.getColumnIndex("id")

        var flag = false
        while(cursor.moveToNext())
        {
            flag = true
            id = cursor.getString(id_cursor).toInt()
        }

        cursor.close()

        if(flag == false)
        {
            return false
        }

        return true
    }


    fun stokSorgula(view : View)
    {
        val parca_adi = parcaAdi.text.toString()
        val parca_markasi = parcaMarka.text.toString()
        val parca_kategorisi = parcaKategori.text.toString()

        if(kontrol(parca_adi, parca_markasi) == false)
        {
            finish()
            return
        }

        if(db_kontrol(database, parca_adi, parca_markasi, parca_kategorisi) == false)
        {
            Toast.makeText(applicationContext, "Aranan Özellikte Parça Bulunamamıştır", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        val intent = Intent(this , StokEkran::class.java)
        intent.putExtra("key",id)
        startActivity(intent)
    }
}