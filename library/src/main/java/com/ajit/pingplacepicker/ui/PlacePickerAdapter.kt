package com.ajit.pingplacepicker.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.libraries.places.api.model.Place
import com.ajit.pingplacepicker.R

class PlacePickerAdapter(private var placeList: List<Place>, private val clickListener: (Place) -> Unit)
    : RecyclerView.Adapter<PlacePickerAdapter.PlaceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {

        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_place, parent, false)

        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(placeList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

    fun swapData(newPlaceList: List<Place>) {
        placeList = newPlaceList
        notifyDataSetChanged()
    }

    inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var ivPlaceType : ImageView
        private lateinit var tvPlaceName : TextView
        private lateinit var tvPlaceAddress : TextView

        fun bind(place: Place, listener: (Place) -> Unit) {

            with(itemView) {
                setOnClickListener { listener(place) }

                ivPlaceType = itemView.findViewById(R.id.ivPlaceType)
                tvPlaceName = itemView.findViewById(R.id.tvPlaceName)
                tvPlaceAddress = itemView.findViewById(R.id.tvPlaceAddress)

                ivPlaceType.setImageResource(UiUtils.getPlaceDrawableRes(itemView.context, place))
                tvPlaceName.text = place.name
                tvPlaceAddress.text = place.address
            }
        }
    }
}

