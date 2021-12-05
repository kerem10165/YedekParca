package com.kerem.yedekparca

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteCursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_araba_modeli_tanimla.*

class ParcaUyumluArabaListesi : AppCompatActivity() {
    lateinit var database : SQLiteDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parca_uyumlu_araba_listesi)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        database = this.openOrCreateDatabase("YedekParcalar" , Context.MODE_PRIVATE, null)

        var list = ArrayList<Int>()
        var cursor = database.rawQuery("SELECT * FROM YedekParcalar" , null)
        val id = cursor.getColumnIndex("id")
        while(cursor.moveToNext())
        {
            list.add(cursor.getString(id).toInt())
        }

        recyclerView.adapter = ParcaUyumluArabaAdapter(list,database)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu , menu)

        var item : MenuItem = menu!!.findItem(R.id.action_search)

        if(item != null){
            var searchView = item.actionView as SearchView

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    var list = ArrayList<Int>()
                    if(newText!!.isNotEmpty())
                    {
                        var cursor = database.rawQuery("SELECT * FROM YedekParcalar WHERE Yedek_Parca_Adi LIKE '${newText}%' or Yedek_Parca_Markasi LIKE '${newText}%' or Yedek_Parca_Kategorisi LIKE '${newText}%'" , null)
                        val id = cursor.getColumnIndex("id")
                        while(cursor.moveToNext())
                        {
                            list.add(cursor.getString(id).toInt())
                        }
                    }
                    else
                    {
                        var cursor = database.rawQuery("SELECT * FROM YedekParcalar" , null)
                        val id = cursor.getColumnIndex("id")
                        while(cursor.moveToNext())
                        {
                            list.add(cursor.getString(id).toInt())
                        }
                    }

                    val adapter = ParcaUyumluArabaAdapter(list,database)
                    recyclerView.adapter = adapter

                    return true
                }
            })
        }

        return super.onCreateOptionsMenu(menu)
    }

}