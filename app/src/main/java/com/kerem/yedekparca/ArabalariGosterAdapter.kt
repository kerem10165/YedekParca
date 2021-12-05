package com.kerem.yedekparca

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_araba.view.*

class ArabalariGosterAdapter (val list : List<Int>?, val database: SQLiteDatabase) : RecyclerView.Adapter<ArabalariGosterAdapter.MyViewHolder>(){
    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_araba , parent , false)
        return ArabalariGosterAdapter.MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var cursor = database.rawQuery("SELECT * FROM Arabalar WHERE id = ${list?.get(position)}" , null)
        println(list?.get(position))
        val id = cursor.getColumnIndex("id")
        val marka = cursor.getColumnIndex("Marka")
        val model = cursor.getColumnIndex("Model")
        val kasa_tipi = cursor.getColumnIndex("Kasa_Tipi")
        val uretim_yili = cursor.getColumnIndex("Uretim_Yili")
        var str = ""
        while(cursor.moveToNext())
        {
            str = "Bu Parça İle Uyumlu Aracın Markası: " + cursor.getString(marka) +
                    "\nModeli: " + cursor.getString(model) +
                    "\nKasa Tipi: " + cursor.getString(kasa_tipi) +
                    "\nÜretim Yılı: " + cursor.getString(uretim_yili)
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