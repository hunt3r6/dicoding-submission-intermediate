package com.hariankoding.storyapp.ui.location

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.hariankoding.storyapp.R
import com.hariankoding.storyapp.databinding.ActivityLocationBinding
import com.hariankoding.storyapp.utils.Result
import com.hariankoding.storyapp.viewmodel.ViewModelFactory

class LocationActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityLocationBinding
    private lateinit var mMap: GoogleMap
    private val viewModel by viewModels<LocationViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setView()
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        addManyMarker()

    }

    private val boundsBuilder = LatLngBounds.Builder()
    private fun addManyMarker() {
        viewModel.getLocation().observe(this) { result ->
            when (result) {
                is Result.Loading -> {

                }
                is Result.Success -> {
                    result.data.forEach { location ->
                        val latLng = LatLng(location.lat, location.lon)
                        mMap.addMarker(MarkerOptions().position(latLng).title(location.name))
                        boundsBuilder.include(latLng)
                    }

                    val bounds: LatLngBounds = boundsBuilder.build()
                    mMap.animateCamera(
                        CameraUpdateFactory.newLatLngBounds(
                            bounds,
                            resources.displayMetrics.widthPixels,
                            resources.displayMetrics.heightPixels,
                            300
                        )
                    )
                }
                is Result.Error -> {
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun setView() = with(binding) {
        toolbar.apply {
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener {
                finish()
            }
            title = getString(R.string.all_location)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isIndoorLevelPickerEnabled = true
            isCompassEnabled = true
            isMapToolbarEnabled = true
        }

        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}