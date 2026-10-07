package com.mirellageri.horoscopo.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mirellageri.horoscopo.adapters.HoroscopeAdapter
import com.mirellageri.horoscopo.data.Horoscope
import com.mirellageri.horoscopo.utils.normalize
import com.mirellageri.horoscopo.utils.search

class MainActivity : androidx.appcompat.app.AppCompatActivity() {

    var horoscopeList: List<Horoscope> = Horoscope.Companion.getAll()
    lateinit var recyclerView : RecyclerView
    lateinit var adapter: HoroscopeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(_root_ide_package_.com.mirellageri.horoscopo.R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(_root_ide_package_.com.mirellageri.horoscopo.R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        recyclerView = findViewById(_root_ide_package_.com.mirellageri.horoscopo.R.id.recyclerView)
        //cuando solo es un parametro no es necesario dar nombre y solo usar el it pero sino de esta manera
        adapter =
            HoroscopeAdapter(horoscopeList) { position ->
                val horoscope = horoscopeList[position]
                //NAVEGAR: intent ayudara a abrir navegadores, usar camara, activar servicios ... // ::class es pasar el plano o tipo de dato del objeto
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("HOROSCOPE_ID", horoscope.id)
                startActivity(intent)
            }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        supportActionBar?.title = getString(_root_ide_package_.com.mirellageri.horoscopo.R.string.horoscope_title)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(_root_ide_package_.com.mirellageri.horoscopo.R.menu.activity_main_menu, menu)
        val searchMenuItem = menu.findItem(_root_ide_package_.com.mirellageri.horoscopo.R.id.search_menu)
        //castear una clase que hereda de otra usando as
        //actionView me devuelve cualquier vista pero con el as le decimos quiero de la busqueda que me devuelvas
        val searchView = searchMenuItem.actionView as SearchView
        //nos ayuda a darnos informacion de lo que esta haciendo
        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                //cuando el usuario ha escrito y luego da a buscar
                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                //mientras el usuario sigue escribiendo buscar pero sirve mas para datos que tiene local
                horoscopeList = Horoscope.Companion.getAll().filter {
                    //dos opciones de usar nuestro stringextension para buqueda
                    getString(it.name).normalize().contains(newText.normalize(),true) ||
                            getString(it.date).search(newText)
                }
                adapter.updateData(horoscopeList)
                return true
            }
        })
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            _root_ide_package_.com.mirellageri.horoscopo.R.id.menuList -> {
                recyclerView.layoutManager = LinearLayoutManager(this)
                true
            }
            _root_ide_package_.com.mirellageri.horoscopo.R.id.menuGrid -> {
                recyclerView.layoutManager = GridLayoutManager(this, 2)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}