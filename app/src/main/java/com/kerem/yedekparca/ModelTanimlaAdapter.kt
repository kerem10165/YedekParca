package com.kerem.yedekparca

import android.content.ContentResolver
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.drawToBitmap
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*
import android.graphics.BitmapFactory
import androidx.core.net.toUri
import java.io.*


class ModelTanimlaAdapter(val list : List<Int>?, val database: SQLiteDatabase) : RecyclerView.Adapter<ModelTanimlaAdapter.MyViewHolder>() {
    private lateinit var con : Context

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item , parent , false)
        con = parent.context
        return MyViewHolder(itemView)
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
        while(cursor.moveToNext())
        {
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

        holder.itemView.yazi.text = str
    }

    override fun getItemCount(): Int {
        if(list == null)
            return 0
        else
            return list.size
    }


}