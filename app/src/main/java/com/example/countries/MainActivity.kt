package com.example.countries

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

class MainActivity : AppCompatActivity() {
    //private var textView:TextView? = null
    private var countriesCoroutine:Job? = null
    private var recyclerViewCountries:RecyclerView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerViewCountries = findViewById(R.id.recyclerViewCountries)
        recyclerViewCountries?.layoutManager = LinearLayoutManager(this)


        countriesCoroutine = CoroutineScope(IO).launch {
            val countriesRepo = CountriesRepository()
            var countries = countriesRepo.doWork()
            //delay(5000) //задержка для имитации долгого вызова
            withContext(Dispatchers.Main) {
                if(countries != null) {
                    recyclerViewCountries?.adapter = CountriesAdapter(countries)

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        countriesCoroutine?.cancel()
    }
}

class Country(val name:String, val capital:String, val nativeName:String)

interface RetrofitCountries {

    @GET("all")
    fun getCountries(): Deferred<Response<List<Country>>>

}

class CountriesRepository {

    suspend fun doWork(): List<Country>? {
        val retrofitPosts = Retrofit
            .Builder()
            .baseUrl("https://restcountries.eu/rest/v2/")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(RetrofitCountries::class.java)
        val result = retrofitPosts
            .getCountries()
            .await()

        return result.body()
    }

}
