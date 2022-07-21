package com.example.vepop.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vepop.R
import com.example.vepop.models.Products

class ProductAdapter(private val productList: ArrayList<Products>):RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_products, parent , false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentItem = productList[position]
        holder.productImage.setImageResource(currentItem.image)
        holder.productPrice.text = currentItem.price
        holder.productLabel.text = currentItem.label
    }

    override fun getItemCount(): Int {
       return productList.size
    }

    class ProductViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val productImage:ImageView = itemView.findViewById(R.id.image)
        val productPrice: TextView = itemView.findViewById(R.id.price)
        val productLabel:TextView = itemView.findViewById(R.id.label)

    }

}

