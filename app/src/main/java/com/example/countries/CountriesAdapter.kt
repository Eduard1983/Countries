package com.example.countries

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.country.view.*

class CountriesAdapter(var countries:List<Country>): RecyclerView.Adapter<CountryHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CountryHolder(inflater,parent)
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    override fun onBindViewHolder(holder: CountryHolder, position: Int) {
        val country = countries[position]
        holder.bind(country)
    }
}

class CountryHolder(inflate:LayoutInflater, parent:ViewGroup):
                    RecyclerView.ViewHolder(inflate.inflate(R.layout.country,parent,false)) {

    private var nameText:TextView = itemView.nameText
    private var capitalText:TextView = itemView.capitalText
    private var nativeNameText:TextView = itemView.nativeNameText

    fun bind(country:Country){
        nameText.text = country.name
        capitalText.text = country.capital
        nativeNameText.text = country.nativeName
    }
}