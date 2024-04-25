package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import com.example.myapplication.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//dc1565d9638f6b0ae1fd66605de5a9d5
class MainActivity : AppCompatActivity() {
    private lateinit var temparature : TextView
    private lateinit var weather : TextView
    private lateinit var maxTemp : TextView
    private lateinit var minTemp : TextView
    private lateinit var humidity : TextView
    private lateinit var windSpeed : TextView
    private lateinit var sunrise : TextView
    private lateinit var sunset : TextView
    private lateinit var sea : TextView
    private lateinit var condition : TextView
    private lateinit var day : TextView
    private lateinit var date : TextView
    private lateinit var cityName2 : TextView
    private lateinit var searchView2 : android.widget.SearchView

    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fetchWeatherData("Hyderabad")
        temparature = findViewById<TextView>(R.id.temperature)
        weather = findViewById<TextView>(R.id.weather)
        maxTemp = findViewById<TextView>(R.id.max_temp)
        minTemp = findViewById<TextView>(R.id.min_temp)
        humidity = findViewById<TextView>(R.id.humidity)
        windSpeed = findViewById<TextView>(R.id.windSpeed)
        sunrise = findViewById<TextView>(R.id.sunrise)
        sunset = findViewById<TextView>(R.id.sunset)
        sea = findViewById<TextView>(R.id.sea)
        condition = findViewById<TextView>(R.id.condition)
        day = findViewById<TextView>(R.id.day)
        date = findViewById<TextView>(R.id.date)
        cityName2 = findViewById<TextView>(R.id.city_name)
        searchView2 = findViewById<android.widget.SearchView>(R.id.searchView)

        SearchCity()
    }

    private fun SearchCity() {
        //val searchView = binding.searchView
        searchView2.setOnQueryTextListener(object:android.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    private fun fetchWeatherData(cityName:String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)

        val response = retrofit.getWeatherData(cityName,"dc1565d9638f6b0ae1fd66605de5a9d5","metric")
        response.enqueue(object:Callback<WeatherApp>{
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
               val responseBody = response.body()
                if (response.isSuccessful && responseBody !=null){
                    val temp = responseBody.main.temp.toString()
                    Log.d("meena","temp: $temp" )
                    val humidity2   = responseBody.main.humidity
                    val windSpeed2   = responseBody.wind.speed
                    val sunRise = responseBody.sys.sunrise.toLong()
                    val sunset2 = responseBody.sys.sunset.toLong()
                    val seaLevel = responseBody.main.pressure
                    val condition2 = responseBody.weather.firstOrNull()?.main?: "unknown"
                    val maxTemp2 = responseBody.main.temp_max
                    val minTemp2 = responseBody.main.temp_min
                    //binding.temperature.text = temp
                    temparature.text = "$temp"
                    weather.text = condition2
                    maxTemp.text = "Max Temp: $maxTemp2"
                    minTemp.text = "Min Temp: $minTemp2"
                    humidity.text = "$humidity2 %"
                    windSpeed.text = "$windSpeed2 m/s"
                    sunrise.text = "${time(sunRise)}"
                    sunset.text = "${time(sunset2)}"
                    sea.text = "$seaLevel hPa"
                    condition.text =  condition2
                    day.text = dayName(System.currentTimeMillis())
                        date.text = date()
                        cityName2.text = "$cityName"
                }
            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun date(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

    fun dayName(timestamp:Long):String{
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format(Date())
    }

    fun time(timestamp:Long):String{
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp*1000))
    }
}




