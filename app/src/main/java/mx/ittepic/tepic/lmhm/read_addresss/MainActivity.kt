package mx.ittepic.tepic.lmhm.read_addresss

import android.content.ContentValues
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.widget.*
import androidx.core.app.ActivityCompat
import java.lang.Exception
import java.util.*

class MainActivity : AppCompatActivity() {
    var permisosCalendario = 1
    var permisosEscrituraC = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.button).setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_CALENDAR)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(
                    android.Manifest.permission.READ_CALENDAR),permisosCalendario)
            }else{
                leerCalendario()
            }
        }
        findViewById<Button>(R.id.button2).setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_CALENDAR)
            !=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(
                    android.Manifest.permission.WRITE_CALENDAR),permisosEscrituraC)
            }else{
                escribirCalendario()
            }
        }
    }

    private fun escribirCalendario() {
        try {
            var titulo = findViewById<EditText>(R.id.titulo).text.toString()
            var decripcion = findViewById<EditText>(R.id.descripcion).text.toString()
            var lugar = findViewById<EditText>(R.id.lugar).text.toString()
            var CV = ContentValues()
            CV.put(CalendarContract.Events.CALENDAR_ID,1)
            CV.put(CalendarContract.Events.TITLE,titulo)
            CV.put(CalendarContract.Events.DESCRIPTION,decripcion)
            CV.put(CalendarContract.Events.EVENT_LOCATION,lugar)
            CV.put(CalendarContract.Events.DTSTART,Calendar.getInstance().timeInMillis)
            CV.put(CalendarContract.Events.DTEND,Calendar.getInstance().timeInMillis +60*60*1000)
            CV.put(CalendarContract.Events.EVENT_TIMEZONE,Calendar.getInstance().timeZone.id)
            contentResolver.insert(CalendarContract.Events.CONTENT_URI,CV)
            Toast.makeText(this,"SE INSERTO EN CALENDARIO",Toast.LENGTH_LONG).show()

        }catch (e:Exception){
            Toast.makeText(this,"NO SE INSERTO EN CALENDARIO",Toast.LENGTH_LONG).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==permisosCalendario){
            leerCalendario()
        }
        if (requestCode==permisosEscrituraC){
            escribirCalendario()
        }
    }

    private fun leerCalendario() {
        var cursor = contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            arrayOf(CalendarContract.Events._ID,
                CalendarContract.Events.DESCRIPTION,
                CalendarContract.Events.TITLE),
            null,
            null,
            null
        )
        var texto = ArrayList<String>()
        if (cursor!!.moveToFirst()){
            do {
                texto.add("ID:${cursor.getString(0)},Descripcion:${cursor.getString(1)},Titulo:${cursor.getString(2)}\n")
            }while (cursor.moveToNext())
            findViewById<ListView>(R.id.lista).adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,texto)

            Toast.makeText(this,"SE ENCONTRARON EVENTOS",Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this,"NO SE ENCONTRARON EVENTOS",Toast.LENGTH_LONG).show()
        }
    }
}