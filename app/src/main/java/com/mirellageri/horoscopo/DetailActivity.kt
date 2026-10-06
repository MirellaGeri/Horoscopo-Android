package com.mirellageri.horoscopo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

class DetailActivity : AppCompatActivity() {
    lateinit var signDetailImageView : ImageView
    lateinit var nameDetailTextView : TextView
    lateinit var dateDetailTextView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //inicializar:
        signDetailImageView = findViewById(R.id.signDetailImageView)
        nameDetailTextView = findViewById(R.id.nameDetailTextView)
        dateDetailTextView = findViewById(R.id.dateDetailTextView)

        //los activity tiene un intent para recibir datos y si o si va a recibir el id le pongo !!
        val id = intent.getStringExtra("HOROSCOPE_ID")!!
        val horoscope = Horoscope.getById(id)

        //mostrar los ruta de datos en los respectivos cajitas de texto:
        signDetailImageView.setImageResource(horoscope.sign)
        nameDetailTextView.setText(horoscope.name)
        dateDetailTextView.setText(horoscope.date)

        supportActionBar?.setTitle(horoscope.name)
        supportActionBar?.setSubtitle(horoscope.date)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //boton atras pero sin funcionalidad
        //supportActionBar?.setHomeAsUpIndicator(image)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_detail_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite_menu ->{
                true
            }
            R.id.share_menu -> {
                shareContent("falta implementear")
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
    private fun shareContent(name : String){
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT,"Este es tu signo: $name")
        }
        startActivity(Intent.createChooser(shareIntent,"Compartir con .."))
    }
}