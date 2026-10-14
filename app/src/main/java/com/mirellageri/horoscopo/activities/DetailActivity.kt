package com.mirellageri.horoscopo.activities

import com.mirellageri.horoscopo.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DetailActivity : AppCompatActivity() {
    lateinit var signDetailImageView: ImageView
    lateinit var nameDetailTextView: TextView
    lateinit var dateDetailTextView: TextView
    lateinit var session: SessionManager
    var isFavorite = false
    lateinit var horoscope: Horoscope
    lateinit var favoriteMenuItem: MenuItem
    lateinit var predictionTextView: TextView
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var progressIndicatorDetail: LinearProgressIndicator
    var predictionToShare = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        //Session:
        session = SessionManager(this)
        //inicializar:
        initialize()

        //los activity tiene un intent para recibir datos y si o si va a recibir el id que le ponga con -> !!
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
        getNavigationView()
        getHoroscopePrediction()
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
            R.id.favorite_menu -> {
                if (isFavorite) {
                    session.setFavorite("")
                } else {
                    session.setFavorite(horoscope.id)
                }
                isFavorite = !isFavorite
                setFavoriteIcon()
                true
            }

            R.id.share_menu -> {
                //Texto a compartir:
                val nameToShare = getString(horoscope.name)
                var textToShare = getString(R.string.horoscope_text_to_share) + " "
                textToShare = textToShare + nameToShare + "\n" + predictionToShare
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
    private fun initialize(){
        signDetailImageView = findViewById(R.id.signDetailImageView)
        nameDetailTextView = findViewById(R.id.nameDetailTextView)
        dateDetailTextView = findViewById(R.id.dateDetailTextView)
        predictionTextView = findViewById(R.id.predictionTextView)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        progressIndicatorDetail = findViewById(R.id.progressIndicatorDetail)
    }
    private fun shareContent(text: String) {
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        startActivity(Intent.createChooser(sendIntent, ""))
    }
    private fun setFavoriteIcon() {
        if (isFavorite) {
            favoriteMenuItem.setIcon(R.drawable.ic_favorite_selected)
        } else {
            favoriteMenuItem.setIcon(R.drawable.ic_favorite)
        }
    }
    private fun getHoroscopePrediction(period:String = "daily"){
        progressIndicatorDetail.show()
        predictionTextView.setText(R.string.horoscope_prediction_loading)
        CoroutineScope(Dispatchers.IO).launch {
            val urlGetRequest =
                URL("https://freehoroscopeapi.com/api/v1/get-horoscope/$period?sign=${horoscope.id}")
            // HTTP Connexion
            val apiConnection = urlGetRequest.openConnection() as HttpURLConnection
            // Method
            apiConnection.setRequestMethod("GET")
            try {
                // Response code
                val responseCode = apiConnection.getResponseCode()
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    //Read the response
                    val response = readInputStream(apiConnection.getInputStream())
                    // Return response
                    val jsonResponse = JSONObject(response)
                    val result = jsonResponse.getJSONObject("data").getString("horoscope")
                    //go to main:
                    CoroutineScope(Dispatchers.Main).launch {
                        predictionToShare = result
                        predictionTextView.text = result
                        progressIndicatorDetail.hide()
                    }
                    Log.w("API REST", result)
                }else {
                    //algo ha salido mal
                    Log.w("API REST", "Status code:$responseCode")
                }
            }catch (e: Exception) {
                    Log.e("API REST", e.localizedMessage, e)
            }finally{
                    apiConnection.disconnect();
            }
        }
    }
    private fun readInputStream(inputStream: InputStream): String {
            // Read the response
            val inTo = BufferedReader(InputStreamReader(inputStream))
            val response = StringBuffer()
            var inputLine: String? = null

            while ((inTo.readLine().also { inputLine = it }) != null) {
                response.append(inputLine);
            }
            inTo.close()
            return response.toString()
    }
    private fun getNavigationView(){
        bottomNavigationView.setOnItemSelectedListener{ item ->
            when(item.itemId){
                R.id.menu_today ->{
                    getHoroscopePrediction()
                    true
                }
                R.id.menu_weekly ->{
                    getHoroscopePrediction("weekly")
                    true
                }R.id.menu_monthly ->{
                    getHoroscopePrediction("monthly")
                    true
                }else -> false
            }
        }
    }
}