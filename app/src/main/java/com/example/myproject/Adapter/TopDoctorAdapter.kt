package com.example.myproject.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.myproject.Activity.DetailActivity
import com.example.myproject.Domain.DoctorsModel
import com.example.myproject.databinding.ViewholderCategoryBinding
import com.example.myproject.databinding.ViewholderTopDoctorBinding

class TopDoctorAdapter(val items:MutableList<DoctorsModel>):RecyclerView.Adapter<TopDoctorAdapter.Viewholder>() {

    private var context: Context?=null

    class Viewholder(val binding: ViewholderTopDoctorBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopDoctorAdapter.Viewholder {
        context = parent.context
        val binding = ViewholderTopDoctorBinding.inflate(LayoutInflater.from(context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: TopDoctorAdapter.Viewholder, position: Int) {
        holder.binding.nameTxt.text = items[position].Name
        holder.binding.specialTxt.text = items[position].Special
        holder.binding.scoreTxt.text = items[position].Rating.toString()
        holder.binding.yearTxt.text = items[position].Expriense.toString() + " Year"
        Glide.with(holder.itemView.context)
            .load(items[position].Picture)
            .apply { RequestOptions().transform(CenterCrop()) }
            .into(holder.binding.img)

        holder.itemView.setOnClickListener{
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("object",items[position])
            context?.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size

}

//package com.example.myproject.Adapter
//
//import android.content.Context
//import android.content.Intent
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.resource.bitmap.CenterCrop
//import com.bumptech.glide.request.RequestOptions
//import com.example.myproject.Activity.DetailActivity
//import com.example.myproject.Domain.DoctorsModel
//import com.example.myproject.databinding.ViewholderTopDoctorBinding
//
//class TopDoctorAdapter(private val items: List<DoctorsModel>) :
//    RecyclerView.Adapter<TopDoctorAdapter.ViewHolder>() {
//    private var context: Context? = null
//
//    class ViewHolder(var binding: ViewholderTopDoctorBinding) :
//        RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        context = parent.context
//        val inflater = LayoutInflater.from(context)
//        val binding = ViewholderTopDoctorBinding.inflate(inflater, parent, false)
//        return ViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val doctor = items[position]
//
//        holder.binding.nameTxt.text = doctor.Name
//        holder.binding.specialTxt.text = doctor.Special
//        holder.binding.scoreTxt.text = doctor.Rating.toString()
//        holder.binding.yearTxt.text = doctor.Expriense.toString() + " year"
//
//        Glide.with(holder.itemView.context)
//            .load(doctor.Picture)
//            .apply(RequestOptions().transform(CenterCrop()))
//            .into(holder.binding.img)
//
//        holder.itemView.setOnClickListener {
//            val intent = Intent(context, DetailActivity::class.java)
//            intent.putExtra("object", doctor)
//            context!!.startActivity(intent)
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return items.size
//    }
//}