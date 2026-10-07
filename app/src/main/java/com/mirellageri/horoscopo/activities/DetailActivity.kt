package com.mirellageri.horoscopo.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mirellageri.horoscopo.data.Horoscope

class DetailActivity : androidx.appcompat.app.AppCompatActivity() {
    lateinit var signDetailImageView : ImageView
    lateinit var nameDetailTextView : TextView
    lateinit var dateDetailTextView : TextView
    lateinit var nameToShare: String
    lateinit var dateToShare: String
    lateinit var textToShare: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(_root_ide_package_.com.mirellageri.horoscopo.R.layout.activity_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(_root_ide_package_.com.mirellageri.horoscopo.R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //inicializar:
        signDetailImageView = findViewById(_root_ide_package_.com.mirellageri.horoscopo.R.id.signDetailImageView)
        nameDetailTextView = findViewById(_root_ide_package_.com.mirellageri.horoscopo.R.id.nameDetailTextView)
        dateDetailTextView = findViewById(_root_ide_package_.com.mirellageri.horoscopo.R.id.dateDetailTextView)

        //los activity tiene un intent para recibir datos y si o si va a recibir el id le pongo !!
        val id = intent.getStringExtra("HOROSCOPE_ID")!!
        val horoscope = Horoscope.Companion.getById(id)

        //mostrar los ruta de datos en los respectivos cajitas de texto:
        signDetailImageView.setImageResource(horoscope.sign)
        nameDetailTextView.setText(horoscope.name)
        dateDetailTextView.setText(horoscope.date)

        //Texto a compartir:
        nameToShare = getString(horoscope.name)
        dateToShare = getString(horoscope.date)
        textToShare = getString(_root_ide_package_.com.mirellageri.horoscopo.R.string.horoscope_text_to_share)
        textToShare = textToShare + nameToShare + "\n" + dateToShare
        //Log.i("HOROSCOPE",textToShare)

        supportActionBar?.setTitle(horoscope.name)
        supportActionBar?.setSubtitle(horoscope.date)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //boton atras pero sin funcionalidad
        //supportActionBar?.setHomeAsUpIndicator(image)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(_root_ide_package_.com.mirellageri.horoscopo.R.menu.activity_detail_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            _root_ide_package_.com.mirellageri.horoscopo.R.id.favorite_menu ->{
                true
            }
            _root_ide_package_.com.mirellageri.horoscopo.R.id.share_menu -> {
                shareContent(textToShare)
                true
            }
            android.R.id.home -> {
                //cierra la pantalla actual para dar paso a la anterior
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun shareContent(text : String){
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT,text)
        }
        startActivity(Intent.createChooser(shareIntent,"Compartir con .."))
    }
}