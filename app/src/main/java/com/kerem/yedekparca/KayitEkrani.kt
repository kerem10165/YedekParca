package com.kerem.yedekparca

import android.R.attr
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_kayit_ekrani.*
import android.graphics.Bitmap

import android.R.attr.bitmap
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception


class KayitEkrani : AppCompatActivity() {
    private val pickImage = 100
    private var imageUri: Uri? = null
    private var bitmap : Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kayit_ekrani)

        resimSec.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
    }

    fun kaydet(view : View)
    {

        val parcaAdi = parca_adi.text.toString()
        val parcaMarkasi = parca_markasi.text.toString()
        val parcaKategorisi = parca_kategorisi.text.toString()
        val aracMarka = arac_marka.text.toString()
        val aracModel = arac_model.text.toString()
        val aracYil = arac_yil.text.toString()
        val stokAdedi = stok_adedi.text.toString()
        val aracKasaTipi = kasa_tipi.text.toString()



        if(parcaAdi == "")
        {
            Toast.makeText(applicationContext, "Parça Adı kısmı boş geçilemez", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        if(parcaMarkasi == "")
        {
            Toast.makeText(applicationContext, "Parça markası kısmı boş geçilemez", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        if(parcaKategorisi == "")
        {
            Toast.makeText(applicationContext, "Parça kategorisi kısmı boş geçilemez", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        if(stokAdedi == "")
        {
            Toast.makeText(applicationContext, "Stok adedi kısmı boş geçilemez", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        if(aracMarka == "")
        {
            Toast.makeText(applicationContext, "Araç markası kısmı boş geçilemez", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        if(aracYil != "")
        {
            val yilInt = aracYil.toInt()
            if(yilInt < 1950 || yilInt > 2023)
            {
                Toast.makeText(applicationContext, "Girilen yıllar istenilen aralıkta değildir(1950-2023)", Toast.LENGTH_LONG).show()
                finish()
                return
            }
        }

        val database = this.openOrCreateDatabase("YedekParcalar" , Context.MODE_PRIVATE, null)

        var command = "SELECT * FROM Arabalar WHERE Marka LIKE '${aracMarka}'"

        if(aracModel != "")
        {
            command+=" AND Model like '${aracModel}'"
        }
        if(aracKasaTipi != "")
        {
            command+=" AND Kasa_Tipi like '${aracKasaTipi}'"
        }
        if(aracYil != "")
        {
            command+=" AND Uretim_Yili like '${aracYil.toInt()}'"
        }

        var cursorAraba = database.rawQuery(command , null)
        var id_arac_cursor = cursorAraba.getColumnIndex("id")
        var id_arac = arrayListOf<Int>()
        var flag = false

        while(cursorAraba.moveToNext())
        {
            id_arac.add(cursorAraba.getString(id_arac_cursor).toInt())
            flag = true
        }

        if(flag == false)
        {
            Toast.makeText(applicationContext, "Belirtilen Türde Bir Araç Bulunamamıştır Lütfen Sistem Yöneticisi İle Görüşün", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        cursorAraba.close()

        var cursorYedekParca = database.rawQuery("SELECT * FROM YedekParcalar WHERE Yedek_Parca_Adi = '${parcaAdi}' AND Yedek_Parca_Markasi LIKE '${parcaMarkasi}'" , null)

        var flag2 = false
        val id_yedek_cursor = cursorYedekParca.getColumnIndex("id")
        var id_yedekparca = 0

        while(cursorYedekParca.moveToNext())
        {
            id_yedekparca = cursorYedekParca.getString(id_yedek_cursor).toInt()
            flag2 = true
        }

        cursorYedekParca.close()

        if(flag2 == false)
        {
            var str = "yok"
            if(bitmap != null) {
                var path = this.getExternalFilesDir(null)

                var dir =  File(path.toString() + "/yedek_parcalar")
                if(!dir.exists())
                    dir.mkdirs()
                var file =  File(dir, "${parcaAdi}_${parcaMarkasi}_${parcaKategorisi}" + ".jpeg")
                str = file.toString()
                var fOut = FileOutputStream(file)
                bitmap!!.compress(Bitmap.CompressFormat.JPEG, 75, fOut)
                fOut.flush();
                fOut.close()
            }
            database.execSQL("INSERT INTO YedekParcalar (Yedek_Parca_Adi , Yedek_Parca_Markasi , Yedek_Parca_Kategorisi , Foto_Id , Stoktaki_Sayisi) VALUES ('${parcaAdi}' , '${parcaMarkasi}' , '${parcaKategorisi}' ,'${str}' , ${stokAdedi?.toInt()})")
            cursorYedekParca = database.rawQuery("SELECT * FROM YedekParcalar WHERE Yedek_Parca_Adi = '${parcaAdi}' AND Yedek_Parca_Markasi LIKE '${parcaMarkasi}'" , null)
            while(cursorYedekParca.moveToNext())
            {
                id_yedekparca = cursorYedekParca.getString(id_yedek_cursor).toInt()
            }
        }

        var cursor_eslesme = database.rawQuery("SELECT * FROM Eslesmeler" , null)
        val id = cursor_eslesme.getColumnIndex("id")
        val araba_id = cursor_eslesme.getColumnIndex("ID_ARABA")
        val parca_id = cursor_eslesme.getColumnIndex("ID_PARCA")
        cursor_eslesme.close()

        id_arac.forEach(){
            cursor_eslesme = database.rawQuery("SELECT * FROM Eslesmeler WHERE ID_ARABA = ${it} AND ID_PARCA = ${id_yedekparca}" , null)
            var f = false
            while(cursor_eslesme.moveToNext())
            {
                f = true
            }

            if(f == false)
            {
                database.execSQL("INSERT INTO Eslesmeler (ID_ARABA , ID_PARCA) VALUES (${it} , ${id_yedekparca})")
            }
            cursor_eslesme.close()


            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
        }
    }

}