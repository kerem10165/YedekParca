package com.kerem.yedekparca

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteCursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*
import kotlinx.android.synthetic.main.list_item.view.title_image
import kotlinx.android.synthetic.main.list_item.view.yazi
import kotlinx.android.synthetic.main.list_parca_araba.view.*
import java.io.File

class ParcaUyumluArabaAdapter (val list : List<Int>?, val database: SQLiteDatabase) : RecyclerView.Adapter<ParcaUyumluArabaAdapter.MyViewHolder>(){
    lateinit var con :  Context

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_parca_araba , parent , false)
        con = parent.context
        return ParcaUyumluArabaAdapter.MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var cursor = database.rawQuery("SELECT * FROM YedekParcalar WHERE id = ${list?.get(position)}" , null)
        val id = cursor.getColumnIndex("id")
        val parca_adi = cursor.getColumnIndex("Yedek_Parca_Adi")
        val parca_markasi = cursor.getColumnIndex("Yedek_Parca_Markasi")
        val parca_kategorisi = cursor.getColumnIndex("Yedek_Parca_Kategorisi")
        val stok = cursor.getColumnIndex("Stoktaki_Sayisi")
        val foto_id = cursor.getColumnIndex("Foto_Id")
        var str = ""
        var foto_adress = ""
        var parca_id = 0


        while(cursor.moveToNext())
        {
            parca_id = cursor.getString(id).toInt()
            str += "Parça Adı : " +cursor.getString(parca_adi) + "\n" +
                    "Parçanın Markası : " + cursor.getString(parca_markasi) + "\n" +
                    "Parça Kategorisi : " + cursor.getString(parca_kategorisi) + "\n" +
                    "Stok Adedi: " + cursor.getString(stok)
            foto_adress = cursor.getString(foto_id)
        }

        if(foto_adress != "yok") {
            var file = File(foto_adress)
            var bitmap = BitmapFactory.decodeFile(file.absolutePath)
            holder.itemView.title_image.setImageBitmap(bitmap)
        }
        else{
            holder.itemView.title_image.setImageResource(R.drawable.none)
        }

        holder.itemView.button.setOnClickListener {
            val intent = Intent(con , ArabalariGoster::class.java)
            intent.putExtra("key" , parca_id)
            con.startActivity(intent)
        }

        holder.itemView.yazi.text = str
    }

    override fun getItemCount(): Int {
        if(list == null)
            return 0
        else
            return list.size
    }
}