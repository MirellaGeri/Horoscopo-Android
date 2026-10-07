package com.mirellageri.horoscopo.utils
import android.content.Context
import androidx.core.content.edit
import android.content.SharedPreferences

class SessionManager(context: Context) {
    val sharedPreference: SharedPreferences = context.getSharedPreferences("horoscope_session", Context.MODE_PRIVATE)
    fun setFavorite(id: String){
        sharedPreference.edit {
            putString("FAVORITE_HOROSCOPE", id)
        }
    }
    fun getFavorite(): String {
        return sharedPreference.getString("FAVORITE_HOROSCOPE","")!!
  }
    fun isFavorite(id: String): Boolean{
        return id == getFavorite()
    }
}