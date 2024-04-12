package com.example.project3

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.project3.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONObject
import java.io.Serializable

class MainActivity : AppCompatActivity(), SpinnerFragment.BreedSelection {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var list = arrayOf<String>()
        val bundle: Bundle = Bundle()


        val catUrl = "https://api.thecatapi.com/v1/breeds?api_key=live_xbkdroHwk7WdpxYupP2HqnifRTufnBFVg4Wq2U0OYHeFtAeGlCKHPgRAOXNzDlZ4"

        val queue = Volley.newRequestQueue(this)

        val stringRequest = StringRequest(Request.Method.GET, catUrl,
            { response ->
                val breedsArray = JSONArray(response)

                for(i in 0 until breedsArray.length()) {
                    val cat : JSONObject = breedsArray.getJSONObject(i)
                    list += cat.getString("name")
                }
                bundle.putStringArray("breed_array", list)
                val spinnerFragment = SpinnerFragment()
                spinnerFragment.setArguments(bundle)

                val fm = supportFragmentManager
                val ft = fm.beginTransaction()
                ft.replace(R.id.spinnerView, spinnerFragment)
                ft.commit()

            },
            { Log.i("MainActivity", "Error retrieving breed data") })
        queue.add(stringRequest)



//        val fm = supportFragmentManager
//        val ft = fm.beginTransaction()
//        ft.replace(R.id.spinnerView, SpinnerFragment())
//        ft.commit()
    }

    override fun onBreedSelected(breedName: String) {
        val url = "https://api.thecatapi.com/v1/breeds/search?name=${breedName}&api_key=live_xbkdroHwk7WdpxYupP2HqnifRTufnBFVg4Wq2U0OYHeFtAeGlCKHPgRAOXNzDlZ4"
        val catData: CatData = CatData()
        val queue = Volley.newRequestQueue(this)
        Log.i("MainActivity", "onBreedSelected called")

        val firstRequest = StringRequest(Request.Method.GET, url,
            {response ->
                val cat = JSONArray(response).getJSONObject(0)

                Log.i("MainActivity", "First response")
                catData.name = breedName
                catData.temperament = cat.getString("temperament")
                catData.origin = cat.getString("origin")

                // Second request inside firstRequest
                val imageRequestUrl = "https://api.thecatapi.com/v1/images/search?api_key=live_xbkdroHwk7WdpxYupP2HqnifRTufnBFVg4Wq2U0OYHeFtAeGlCKHPgRAOXNzDlZ4&breed_ids=${cat.getString("id")}"

                val secondRequest = StringRequest(Request.Method.GET, imageRequestUrl,
                    {res ->
                        val imageURL = JSONArray(res).getJSONObject(0).getString("url")

                        println(imageURL)
                        catData.imageUrl = imageURL
                        Log.i("MainActivity", "Second response")

                        val fm = supportFragmentManager
                        val ft = fm.beginTransaction()
                        val bottomFragment: BottomFragment = BottomFragment.newInstance(catData)

                        ft.replace(R.id.bottomLayout, bottomFragment)
                        ft.commit()
                    },
                    {
                        Log.i("MainActivity", "Error retrieving image url!")
                    })
                queue.add(secondRequest)
                // End second request
//                val bottomFragment = supportFragmentManager.findFragmentById(R.id.bottomLayout) as BottomFragment


            },
            {
                Log.i("MainActivity", "Error retrieving specific cat data!")
            })
        queue.add(firstRequest)


    }

}

class CatData( var name: String? = null,
               var temperament: String? = null,
               var imageUrl : String? = null,
               var origin: String? = null): Serializable {
}