package com.example.breakfast.ui.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.breakfast.R
import com.example.breakfast.ui.home.HomeFragment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray





class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var myProductName: LinearLayout
    private var FIREBASE_REF: String = "FIREBASE_REF"


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {


        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)

        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.textSize = 20F
            textView.text = it
        })

        myProductName = root.findViewById(R.id.shopping_cart)


        /* get order here*/
        //val order = this.arguments?.getString(HomeFragment.ORDER) //real data
        val order = menu() // test data
        var totalPrice = 0

        if (order != null) {
            totalPrice = showOrder(order)
        }



        // create total
        val total = TextView(context)
        total.textSize = 20f
        total.text = "Total: $totalPrice"

        myProductName.addView(total)

        // create send button
        val send = Button(context)
        send.textSize = 20f
        send.text = "Send"

        myProductName.addView(send)


        send.setOnClickListener{
            if (order != null) {
                sendOrder(order)
            }
        }


        return root
    }


    @SuppressLint("SetTextI18n")
    private fun showOrder(orderJson: String): Int{

        val products: JsonArray? = Gson().fromJson(orderJson, JsonArray::class.java)

        var total = 0
        if (products != null) {
            for(product in products){

                // create text view
                val productContext = TextView(context)
                productContext.textSize = 20f
                productContext.text =
                    "Name:       " + product.asJsonObject.get("name").asString + "\n" +
                            "Price:        " + product.asJsonObject.get("price").asString+ "\n" +
                            "Quantity:   " + product.asJsonObject.get("quantity").asString


                val productPicture = ImageView(context)
                productPicture.setImageResource(product.asJsonObject.get("picture").asInt)

                // create card
                val card = context?.let { CardView(it) }

                // set card
                card?.setCardBackgroundColor(1)
                card?.minimumHeight = 50
                card?.minimumWidth = 50
                card?.radius = 20f
                card?.addView(productPicture)
                card?.addView(productContext)


                myProductName.addView(card)

                // count total
                total += (product.asJsonObject.get("price").asInt) * (product.asJsonObject.get("quantity").asInt)
            }
        }

        return total
    }

    /* send order to firebase */
    private fun sendOrder(order: String) {

        //val ref = Firebase(FIREBASE_REF)
        //ref.setValue(order)
    }

    private fun menu(): String? {

        val products = ArrayList<HomeFragment.Product>()

        products.add(HomeFragment.Product("toast", 30, 1, R.drawable.ic_home_black_24dp))
        products.add(HomeFragment.Product("ACB", 20, 2, R.drawable.ic_home_black_24dp))
        products.add(HomeFragment.Product("vdf", 70, 3, R.drawable.ic_home_black_24dp))
        products.add(HomeFragment.Product("hbbh", 60, 4, R.drawable.ic_home_black_24dp))

        val gson = GsonBuilder().setPrettyPrinting().create()

        return gson.toJson(products)
    }
}
