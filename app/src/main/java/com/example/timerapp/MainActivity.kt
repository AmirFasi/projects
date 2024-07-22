package com.example.timerapp

import android.os.Bundle
import android.os.Handler
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    // UI components
    private lateinit var countrySpinner: Spinner
    private lateinit var timeTextView: TextView

    // Handler for periodic updates
    private val handler = Handler()

    // Time format including AM/PM
    private val timeFormat = SimpleDateFormat("HH:MM:SS a", Locale.getDefault())

    // Static map of countries to their primary time zones
    private val countryTimeZones = mapOf(
        "United States" to "America/New_York",
        "United Kingdom" to "Europe/London",
        "France" to "Europe/Paris",
        "Germany" to "Europe/Berlin",
        "India" to "Asia/Kolkata",
        "China" to "Asia/Shanghai",
        "Japan" to "Asia/Tokyo",
        "Australia" to "Australia/Sydney",
        "Brazil" to "America/Sao_Paulo",
        "Canada" to "America/Toronto",
        "Mexico" to "America/Mexico_City",
        "Russia" to "Europe/Moscow",
        "South Africa" to "Africa/Johannesburg",
        "Italy" to "Europe/Rome",
        "Spain" to "Europe/Madrid",
        "Netherlands" to "Europe/Amsterdam",
        "Turkey" to "Europe/Istanbul",
        "Argentina" to "America/Argentina/Buenos_Aires",
        "New Zealand" to "Pacific/Auckland",
        "Saudi Arabia" to "Asia/Riyadh",
        "Iran" to "Asia/Tehran",
        "United Arab Emirates" to "Asia/Dubai",
        "South Korea" to "Asia/Seoul",
        "Singapore" to "Asia/Singapore",
        "Thailand" to "Asia/Bangkok",
        "Malaysia" to "Asia/Kuala_Lumpur",
        "Vietnam" to "Asia/Ho_Chi_Minh",
        "Pakistan" to "Asia/Karachi",
        "Bangladesh" to "Asia/Dhaka",
        "Colombia" to "America/Bogota",
        "Chile" to "America/Santiago",
        "Peru" to "America/Lima",
        "Poland" to "Europe/Warsaw",
        "Greece" to "Europe/Athens"
        // Add more countries and their primary time zones here
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        // Initialize UI components
        countrySpinner = findViewById(R.id.countrySpinner)
        timeTextView = findViewById(R.id.timeTextView)

        // Create a sorted list of countries from the map
        val countryList = countryTimeZones.keys.toList().sorted()

        // Set up the ArrayAdapter to display the list of countries in the Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countryList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        countrySpinner.adapter = adapter

        // Set up the Spinner item selection listener
        countrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: android.view.View,
                position: Int,
                id: Long
            ) {
                // Get the selected country from the Spinner
                val selectedCountry = countryList[position]
                // Update the time display for the selected country
                updateTime(selectedCountry)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No action needed when nothing is selected
            }
        }
    }

    // Updates the time display based on the selected country
    private fun updateTime(country: String) {
        // Get the time zone ID for the selected country, default to GMT if not found
        val timeZoneId = countryTimeZones[country] ?: "GMT"

        // Remove any previously scheduled callbacks to avoid multiple updates
        handler.removeCallbacksAndMessages(null)
        // Post a new Runnable to update the time every second
        handler.post(object : Runnable {
            override fun run() {
                // Get the current time in the specified time zone
                val calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZoneId))
                timeFormat.timeZone = calendar.timeZone
                val time = timeFormat.format(calendar.time)
                // Update the TextView with the formatted time
                timeTextView.text = time
                // Schedule the next update after 1 second
                handler.postDelayed(this, 1000)
            }
        })
    }
}
