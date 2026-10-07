package com.mirellageri.horoscopo.adapters

import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.mirellageri.horoscopo.data.Horoscope
import com.mirellageri.horoscopo.R
import com.mirellageri.horoscopo.utils.SessionManager

class HoroscopeAdapter(
    var items: List<Horoscope>,
    //estamos creando una funcion lambda simple que no devuelva resultado pero si recibe un parametro
    val onItemClick:(position:Int) -> Unit )
: RecyclerView.Adapter<HoroscopeViewHolder>() {
    //cual es la vista de cada elemento
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoroscopeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_horoscope,parent,false)
        return HoroscopeViewHolder(view)
    }
    //cuales son los datos del elemento que esta en tal posicion
    override fun onBindViewHolder(holder: HoroscopeViewHolder, position: Int) {
        val horoscope = items[position]
        //mostrar horoscopo
        holder.render(horoscope)
        //me dice mi viewholder cuando le das clic a all itemview que esta en el holder osea a lo que se muestra como cada celda
        holder.itemView.setOnClickListener {
            //Navegar al detalle
            onItemClick(position)
        }
    }
    //cuantos elementos tengo que mostrar
    override fun getItemCount(): Int {
        return items.size
    }
    fun updateData(dataSet: List<Horoscope>){
        items = dataSet
        notifyDataSetChanged()
    }
}

class HoroscopeViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val signImageView: ImageView = view.findViewById(R.id.signImageView)
    val nameTextView: TextView = view.findViewById(R.id.nameTextView)
    val dateTextView: TextView = view.findViewById(R.id.datesTextView)
    val favoriteImageView: ImageView = view.findViewById(R.id.favoriteImageView)

    fun render(horoscope: Horoscope){
        nameTextView.setText(horoscope.name)
        dateTextView.setText(horoscope.date)
        signImageView.setImageResource(horoscope.sign)
        /*if(SessionManager(itemView.context).isFavorite(horoscope.id)){
            favoriteImageView.visibility = View.VISIBLE
        }else{
            favoriteImageView.visibility = View.GONE
        }*/
        favoriteImageView.isVisible = SessionManager(itemView.context).isFavorite(horoscope.id)
    }
}