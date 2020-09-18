package hr.stipanic.tomislav.thrifty.ui.profile_screen.add_screen

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import hr.stipanic.tomislav.thrifty.R
import hr.stipanic.tomislav.thrifty.utils.Constants.CATEGORY_EXTRA
import hr.stipanic.tomislav.thrifty.utils.Constants.CITY_EXTRA
import hr.stipanic.tomislav.thrifty.utils.Constants.COUNTRY_EXTRA
import hr.stipanic.tomislav.thrifty.utils.Constants.DESC_EXTRA
import hr.stipanic.tomislav.thrifty.utils.Constants.LAT_EXTRA
import hr.stipanic.tomislav.thrifty.utils.Constants.LNG_EXTRA
import hr.stipanic.tomislav.thrifty.utils.Constants.LOCATION_PERMISSION_REQUEST_CODE
import hr.stipanic.tomislav.thrifty.utils.Constants.PRICE_EXTRA
import hr.stipanic.tomislav.thrifty.utils.Constants.TITLE_EXTRA
import hr.stipanic.tomislav.thrifty.utils.PermissionUtil.PermissionDeniedDialog.Companion.newInstance
import hr.stipanic.tomislav.thrifty.utils.PermissionUtil.isPermissionGranted
import hr.stipanic.tomislav.thrifty.utils.PermissionUtil.requestPermission
import kotlinx.android.synthetic.main.fragment_add_location.*

class AddLocationFragment : Fragment(), OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback {

    private lateinit var navController: NavController
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var map: GoogleMap
    private lateinit var latLng: LatLng
    private var country = "Unknown"
    private var city = "Unknown"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = findNavController()

        val lm: LocationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gpsEnabled = false
        var networkEnabled = false

        gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!gpsEnabled && !networkEnabled) {
            // notify user
            AlertDialog.Builder(requireContext())
                .setMessage(R.string.gps_network_not_enabled)
                .setNegativeButton(R.string.Cancel, null)
                .setPositiveButton(
                    R.string.open_location_settings
                ) { _, _ -> requireContext().startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
                .show()
        } else {
            val mapFragment =
                childFragmentManager.findFragmentById(R.id.googleMapFragment) as SupportMapFragment?
            mapFragment?.getMapAsync(this)

            addLocationNextButton.setOnClickListener { goToAddImageFragment() }
            addLocationBackButton.setOnClickListener { navController.popBackStack() }
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        }
    }


    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap ?: return
        enableMyLocation()
    }

    private fun enableMyLocation() {
        if (!::map.isInitialized) return
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                val lat = location?.latitude
                val lon = location?.longitude
                if (lat != null && lon != null) {
                    val latLng = LatLng(lat, lon)
                    setLatLng(latLng)
                    val geocoder = Geocoder(requireContext())
                    val address = geocoder.getFromLocation(lat, lon, 1)
                    country = address[0].countryName
                    city = address[0].locality
                    addLocationTextView.text = resources.getString(
                        R.string.addLocationCurrentLocation,
                        city,
                        country
                    )
                    map.addMarker(MarkerOptions().position(latLng))
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f))
                }
            }

        } else {
            requestPermission(
                requireActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION, true
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return
        }
        if (isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            enableMyLocation()
        } else {
            showMissingPermissionError()
        }
    }

    private fun showMissingPermissionError() {
        newInstance(true).show(childFragmentManager, "dialog")
    }

    private fun setLatLng(latLng: LatLng) {
        this.latLng = latLng
    }


    private fun goToAddImageFragment() {
        val category = requireArguments().getString(CATEGORY_EXTRA)!!
        val title = requireArguments().getString(TITLE_EXTRA)!!
        val desc = requireArguments().getString(DESC_EXTRA)!!
        val price = requireArguments().getFloat(PRICE_EXTRA)
        val bundle = bundleOf(
            Pair(CATEGORY_EXTRA, category),
            Pair(TITLE_EXTRA, title),
            Pair(DESC_EXTRA, desc),
            Pair(LAT_EXTRA, latLng.latitude.toFloat()),
            Pair(LNG_EXTRA, latLng.longitude.toFloat()),
            Pair(COUNTRY_EXTRA, country),
            Pair(CITY_EXTRA, city),
            Pair(PRICE_EXTRA, price)
        )
        navController.navigate(R.id.action_addLocationFragment_to_addImageFragment, bundle)
    }
}