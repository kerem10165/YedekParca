package com.kerem.yedekparca

import android.content.Context
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_stok_ekran.*
import kotlinx.android.synthetic.main.list_item.view.*
import java.io.File

class StokEkran : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stok_ekran)

        val id = intent.getIntExtra("key" , 0)

        var database = this.openOrCreateDatabase("YedekParcalar" , Context.MODE_PRIVATE, null)

        var cursor = database.rawQuery("SELECT * FROM YedekParcalar WHERE id = ${id}" , null)

        val parca_adi = cursor.getColumnIndex("Yedek_Parca_Adi")
        val parca_markasi = cursor.getColumnIndex("Yedek_Parca_Markasi")
        val parca_kategorisi = cursor.getColumnIndex("Yedek_Parca_Kategorisi")
        val foto_id = cursor.getColumnIndex("Foto_Id")
        val stok = cursor.getColumnIndex("Stoktaki_Sayisi")

        var str = ""
        var foto_adress = ""
        while(cursor.moveToNext())
        {
            str += "Parça Adı: " + cursor.getString(parca_adi) + "\nParça Markası: " + cursor.getString(parca_markasi) +
            "\nParça Kategorisi: " +cursor.getString(parca_kategorisi) + "\n\nStok Sayısı: " + cursor.getString(stok)
            foto_adress = cursor.getString(foto_id)
        }

        println(foto_adress)

        if(foto_adress != "yok") {
            var file = File(foto_adress)
            var bitmap = BitmapFactory.decodeFile(file.absolutePath)
            parcaResmi.setImageBitmap(bitmap)
        }

        textView3.text = str

        stokButton.setOnClickListener(){
            var sharedPreferences = this.getSharedPreferences("parca_talep" , Context.MODE_PRIVATE)
            sharedPreferences.edit().putInt("${id}" , 1).apply()
            Toast.makeText(applicationContext, "Daha Fazla Stok Talep Edilmiştir", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}