package com.kerem.yedekparca

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_araba_modeli_tanimla.*
import java.io.BufferedReader
import java.io.InputStreamReader

class ArabaModeliTanimla : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_araba_modeli_tanimla)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
    }

    fun db_bastir(arabaMarka : String , arabaModel : String , arabaUretimYili : String) : HashSet<Int>?
    {
        val database = this.openOrCreateDatabase("YedekParcalar" , Context.MODE_PRIVATE, null)
        var command = "SELECT * FROM Arabalar WHERE Marka LIKE '${arabaMarka}%' And Model LIKE '${arabaModel}%'"
        if(arabaUretimYili!="")
        {
            command+= " AND Uretim_Yili = ${arabaUretimYili.toInt()}"
        }

        var cursorAraba = database.rawQuery(command , null)
        val araba_id = cursorAraba.getColumnIndex("id")
        val marka = cursorAraba.getColumnIndex("Marka")
        val model = cursorAraba.getColumnIndex("Model")
        val uretim_yili = cursorAraba.getColumnIndex("Uretim_Yili")

        var listArabaId = ArrayList<Int>()

        while(cursorAraba.moveToNext())
        {
            listArabaId.add(cursorAraba.getInt(araba_id))
        }

        if(listArabaId.size == 0)
        {
            Toast.makeText(applicationContext, "Belirtilen Özellikte Araç bulunamamıştır", Toast.LENGTH_LONG).show()
            return hashSetOf<Int>()
        }

        var hash = hashSetOf<Int>()

        listArabaId.forEach(){
            command = "SELECT * FROM Eslesmeler WHERE ID_ARABA = ${it}"
            var cursorEslesme = database.rawQuery(command , null)
            val id_parca = cursorEslesme.getColumnIndex("ID_PARCA")
            while(cursorEslesme.moveToNext())
            {
                hash.add(cursorEslesme.getInt(id_parca))
            }
            cursorEslesme.close()
        }

        cursorAraba.close()
        return hash
    }

    fun filtrele(view : View)
    {
        val marka = arabaMarkasiGirme.text.toString().lowercase()
        val model = modelGirme.text.toString().lowercase()
        val yil = yilGirme.text.toString()

        if(marka == "")
        {
            Toast.makeText(applicationContext, "Marka kısmı boş geçilemez", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        if(yil != "")
        {
            val yilInt = yil.toInt()
            if(yilInt < 1950 || yilInt > 2023)
            {
                Toast.makeText(applicationContext, "Girilen yıllar istenilen aralıkta değildir(1950-2023)", Toast.LENGTH_LONG).show()
                finish()
                return
            }
        }

        val hash = db_bastir(marka,model,yil)
        val list = hash?.toList()
        val database = this.openOrCreateDatabase("YedekParcalar" , Context.MODE_PRIVATE, null)
        val adapter = ModelTanimlaAdapter(list,database)
        recyclerView.adapter = adapter
    }
}