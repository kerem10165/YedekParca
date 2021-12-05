package com.kerem.yedekparca

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_araba_modeli_tanimla.*
import kotlinx.android.synthetic.main.activity_arabalari_goster.*

class ArabalariGoster : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arabalari_goster)

        var key = getIntent().getIntExtra("key" , 0)
        val database = this.openOrCreateDatabase("YedekParcalar" , Context.MODE_PRIVATE, null)
        var cursor = database.rawQuery("SELECT * FROM Eslesmeler WHERE ID_PARCA = ${key}" , null)
        var list = ArrayList<Int>()
        val araba_id = cursor.getColumnIndex("ID_ARABA")

        while(cursor.moveToNext())
        {
            list.add(cursor.getString(araba_id).toInt())
        }

        if(list.size == 0)
        {
            Toast.makeText(applicationContext, "Sistemde Bu Parçayla Uyumlu Araç Bulunamamıştır", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        val layoutManager = LinearLayoutManager(this)
        showAraba.layoutManager = layoutManager
        showAraba.adapter = ArabalariGosterAdapter(list,database)
    }
}