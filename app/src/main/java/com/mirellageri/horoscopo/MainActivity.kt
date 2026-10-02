package com.mirellageri.horoscopo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    val horoscopeList: List<Horoscope> =listOf(
        Horoscope("aries",R.string.horoscope_name_aries,R.string.horoscope_date_aries,R.drawable.aries_icon),
        Horoscope("taurus", R.string.horoscope_name_taurus, R.string.horoscope_date_taurus, R.drawable.taurus_icon),
        Horoscope("gemini", R.string.horoscope_name_gemini, R.string.horoscope_date_gemini, R.drawable.gemini_icon),
        Horoscope("cancer", R.string.horoscope_name_cancer, R.string.horoscope_date_cancer, R.drawable.cancer_icon),
        Horoscope("leo", R.string.horoscope_name_leo, R.string.horoscope_date_leo, R.drawable.leo_icon),
        Horoscope("virgo", R.string.horoscope_name_virgo, R.string.horoscope_date_virgo, R.drawable.virgo_icon),
        Horoscope("libra", R.string.horoscope_name_libra, R.string.horoscope_date_libra, R.drawable.libra_icon),
        Horoscope("scorpio", R.string.horoscope_name_scorpio, R.string.horoscope_date_scorpio, R.drawable.scorpio_icon),
        Horoscope("sagittarius", R.string.horoscope_name_sagittarius, R.string.horoscope_date_sagittarius, R.drawable.sagittarius_icon),
        Horoscope("capricorn", R.string.horoscope_name_capricorn, R.string.horoscope_date_capricorn, R.drawable.capricorn_icon),
        Horoscope("aquarius", R.string.horoscope_name_aquarius, R.string.horoscope_date_aquarius, R.drawable.aquarius_icon),
        Horoscope("pisces", R.string.horoscope_name_pisces, R.string.horoscope_date_pisces, R.drawable.pisces_icon)
    )
    lateinit var recyclerView : RecyclerView
    lateinit var adapter: HoroscopeAdapter
    lateinit var menuToolBar : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        menuToolBar = findViewById(R.id.menuToolBar)
        setSupportActionBar(menuToolBar)
        recyclerView = findViewById(R.id.recyclerView)
        //cuando solo es un parametro no es necesario dar nombre y solo usar el it pero sino de esta manera
        adapter = HoroscopeAdapter(horoscopeList) { position ->
            val horoscope = horoscopeList[position]
            //NAVEGAR: intent ayudara a abrir navegadores, usar camara, activar servicios ... // ::class es pasar el plano o tipo de dato del objeto
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("HOROSCOPE_ID",horoscope.id)
            intent.putExtra("HOROSCOPE_NAME",horoscope.name)
            intent.putExtra("HOROSCOPE_DATE",horoscope.date)
            intent.putExtra("HOROSCOPE_SIGN",horoscope.sign)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_principal, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

            R.id.menuList -> {
                recyclerView.layoutManager = LinearLayoutManager(this)
                true
            }

            R.id.menuGrid -> {
                recyclerView.layoutManager = GridLayoutManager(this, 2)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}