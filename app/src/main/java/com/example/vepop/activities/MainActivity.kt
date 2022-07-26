package com.example.vepop.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vepop.R
import com.example.vepop.adapters.ProductAdapter
import com.example.vepop.models.Products
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

class MainActivity : AppCompatActivity() {

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<Products>
    lateinit var imageId: Array<Int>
    lateinit var labels: Array<String>
    lateinit var prices: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        slider()

        imageId = arrayOf(
            R.drawable.a,
            R.drawable.b,
            R.drawable.c,
            R.drawable.d,
            R.drawable.e,
            R.drawable.f,
        )

        labels = arrayOf(
            "T-Shirt 1",
            "T-Shirt 2",
            "T-Shirt 3",
            "T-Shirt 4",
            "T-Shirt 5",
            "T-Shirt 6",
        )

        prices = arrayOf(
            "₹ 699.00",
            "₹ 699.00",
            "₹ 699.00",
            "₹ 699.00",
            "₹ 699.00",
            "₹ 699.00",
        )

        newRecyclerView = findViewById(R.id.rvProducts)
        newRecyclerView.layoutManager = GridLayoutManager(this, 2)
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf<Products>()
        getUserdata()
    }

    private fun getUserdata() {
        for(i in imageId.indices){

            val product = Products(imageId[i],labels[i],prices[i])
            newArrayList.add(product)
        }

        newRecyclerView.adapter = ProductAdapter(newArrayList)
    }


    //function for image slider
    private fun slider(){
        val carousel: ImageCarousel = findViewById(R.id.carousel)

// Register lifecycle. For activity this will be lifecycle/getLifecycle() and for fragment it will be viewLifecycleOwner/getViewLifecycleOwner().
        carousel.registerLifecycle(lifecycle)

        val list = mutableListOf<CarouselItem>()

// Image URL with caption
        list.add(
            CarouselItem(
                imageUrl = "https://images.unsplash.com/photo-1483985988355-763728e1935b?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80",
                caption = "Latest Clothing Selections"
            )

        )
        list.add(
            CarouselItem(
                imageUrl = "https://images.unsplash.com/photo-1508427953056-b00b8d78ebf5?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80",
                caption = "Random good looking Angrez"
            )
        )
        list.add(
            CarouselItem(
                imageUrl = "https://images.unsplash.com/photo-1495105787522-5334e3ffa0ef?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1170&q=80",
                caption = "Aisa shirt mujhe bhi chahiye"
            )
        )
        carousel.setData(list)
    }
}