package com.mirellageri.horoscopo.activities

import com.mirellageri.horoscopo.R
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
import com.mirellageri.horoscopo.utils.SessionManager
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {
    lateinit var signDetailImageView : ImageView
    lateinit var nameDetailTextView : TextView
    lateinit var dateDetailTextView : TextView
    lateinit var session: SessionManager
    var isFavorite = false
    lateinit var horoscope: Horoscope
    lateinit var favoriteMenuItem: MenuItem

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

        //Session:
        session = SessionManager(this)

        //los activity tiene un intent para recibir datos y si o si va a recibir el id le pongo !!
        val id = intent.getStringExtra("HOROSCOPE_ID")!!
        horoscope = Horoscope.Companion.getById(id)

        //mostrar los ruta de datos en los respectivos cajitas de texto:
        signDetailImageView.setImageResource(horoscope.sign)
        nameDetailTextView.setText(horoscope.name)
        dateDetailTextView.setText(horoscope.date)

        supportActionBar?.setTitle(horoscope.name)
        supportActionBar?.setSubtitle(horoscope.date)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //boton atras pero sin funcionalidad
        //supportActionBar?.setHomeAsUpIndicator(image)

        //Preguntar si FAvorite is true o false para rellenar el corazon
        isFavorite = session.isFavorite(id)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_detail_menu, menu)
        //despues que el menu se haya creado recien pintar si es true
        favoriteMenuItem = menu.findItem(R.id.favorite_menu)
        setFavoriteIcon()
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite_menu ->{
                if (isFavorite){
                    session.setFavorite("")
                }else{
                    session.setFavorite(horoscope.id)
                }
                isFavorite = !isFavorite
                setFavoriteIcon()
                true
            }
            R.id.share_menu -> {
                //Texto a compartir:
                val nameToShare = getString(horoscope.name)
                val dateToShare = getString(horoscope.date)
                var textToShare = getString(R.string.horoscope_text_to_share)+" "
                textToShare = textToShare + nameToShare + "\n" + dateToShare
                //Log.i("HOROSCOPE",textToShare)
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
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT,text)
        }
        startActivity(Intent.createChooser(sendIntent,"Compartir con .."))
    }
    fun setFavoriteIcon(){
        if (isFavorite){
            favoriteMenuItem.setIcon(R.drawable.ic_favorite_selected)
        }else{
            favoriteMenuItem.setIcon(R.drawable.ic_favorite)
        }
    }
}