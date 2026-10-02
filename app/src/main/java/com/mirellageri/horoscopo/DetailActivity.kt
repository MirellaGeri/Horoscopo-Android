package com.mirellageri.horoscopo

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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

        //los activity tiene un intent
        val id = intent.getStringExtra("HOROSCOPE_ID")
        val name = intent.getIntExtra("HOROSCOPE_NAME",0)
        val date = intent.getIntExtra("HOROSCOPE_DATE",0)
        val image = intent.getIntExtra("HOROSCOPE_SIGN",0)
        //Toast.makeText(this, id, Toast.LENGTH_SHORT).show()

        signDetailImageView.setImageResource(image)
        nameDetailTextView.setText(name)
        dateDetailTextView.setText(date)
    }
}