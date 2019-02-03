package com.example.sollo.chat

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.good_row.view.*

class HomeAdapter: RecyclerView.Adapter<CustomViewHolder>(){

    val goodNames = listOf("item", "item1", "item2")

    //number of items
    override fun getItemCount(): Int {
        return goodNames.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(p0.context)
        val cellForRow = layoutInflater.inflate(R.layout.good_row, p0, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(p0: CustomViewHolder, p1: Int) {
        val goodName = goodNames.get(p1)
        p0.view.textViewGoodName.text = goodName

    }
}

class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view) {

}
