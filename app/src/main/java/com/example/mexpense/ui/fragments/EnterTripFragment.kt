package com.example.mexpense.ui.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mexpense.data.trip.Trip
import com.example.mexpense.databinding.FragmentEnterTripBinding
import com.example.mexpense.MExpenseApplication
import com.example.mexpense.R
import com.example.mexpense.TripViewModel
import com.example.mexpense.TripViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import java.text.SimpleDateFormat
import java.util.*


class EnterTripFragment : Fragment() {
    private var _binding: FragmentEnterTripBinding? = null
    private val binding get() = _binding!!
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    //init the variables lat for latitude and long for longitude
    private var lat: Double = 0.0
    private var long: Double = 0.0

    private val viewModel: TripViewModel by activityViewModels {
        TripViewModelFactory(
            //Use the database instance you created in one of the previous tasks to call the itemDao constructor.
            (activity?.application as MExpenseApplication).database.tripDao()
        )
    }
    lateinit var trip: Trip
    private var tripRisk: String = "No"

    private fun onCheckboxClicked(view: View) {
        if (view is CheckBox) {
            val checked: Boolean = view.isChecked

            when (view.id) {
                R.id.riskCheckBox -> {
                    if (checked) {
                        tripRisk = "Yes"
                    } else {
                        tripRisk = "No"
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnterTripBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.tripName.text.toString(),
            binding.tripLocation.text.toString(),
            binding.tripTime.text.toString(),
            tripRisk
        )
    }

    private fun addNewItem() {
        if (isEntryValid()) {
            val action = EnterTripFragmentDirections.actionEnterTripFragmentToConfirmTripFragment(
                binding.tripName.text.toString(),
                binding.tripLocation.text.toString(),
                binding.tripTime.text.toString(),
                tripRisk,
                binding.tripDescription.text.toString(),
            )
            findNavController().navigate(action)
        }
        else{
            Toast.makeText(requireContext(), "Please fill in all the required fields.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //location stuff here
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        setCoordinates()

        binding.apply {
            binding.ButtonEnter.setOnClickListener {
                addNewItem()
            }
            binding.tripTime.transformIntoDatePicker(requireContext(), "MM/dd/yyyy")
            binding.riskCheckBox.setOnCheckedChangeListener { view, _ ->
                onCheckboxClicked(view)
            }
            binding.getLocationButton.setOnClickListener {
                setCoordinates()
                getUserLocation()
            }
        }
    }

    private fun getUserLocation() {
        if (!checkLocationPermission()){
            //if user hasn't granted location permission yet, do this
            getPermission()
            Toast.makeText(requireContext(), "Location permission has not been granted", Toast.LENGTH_SHORT).show()
        }
        else{
            val currentLat = lat
            val addressInfo = getAddressInfo(lat, long)
            if (addressInfo == "Location initializing"){
                Toast.makeText(requireContext(), "Location services initializing, please try again.", Toast.LENGTH_SHORT).show()
            }
            else{
                binding.tripLocation.setText(addressInfo)
            }
        }
    }

    private fun getPermission() {
        //if permission isn't granted, asks the user for it
        if (ActivityCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101)
        }
    }


    private fun setCoordinates() {
        val priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        val cancellationTokenSource = CancellationTokenSource()

        if (ActivityCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        //first, checks if the location service toggle is enabled on the device. If enabled, get the
        //current location and pass it to the location variables using setCoordinates()
        if (isLocationEnabled(requireContext())) {
            fusedLocationProviderClient.getCurrentLocation(
                priority, cancellationTokenSource.token
            )
                .addOnSuccessListener { location ->
                    setCoordinates(location.latitude, location.longitude)
                }
        }
        return
    }

    private fun setCoordinates(thisLat: Double, thisLong: Double){
        lat = thisLat
        long = thisLong
    }

    //checks if the location service toggle is enabled
    private fun isLocationEnabled(mContext: Context): Boolean {
        val lm = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || lm.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    private fun EditText.transformIntoDatePicker(context: Context, format: String) {
        isFocusableInTouchMode = false
        isClickable = true
        isFocusable = false

        val myCalendar = Calendar.getInstance()
        val datePickerOnDataSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val sdf = SimpleDateFormat(format, Locale.UK)
                setText(sdf.format(myCalendar.time))
            }

        setOnClickListener {
            DatePickerDialog(
                context, datePickerOnDataSetListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).run {
                show()
            }
        }
    }

    private fun checkLocationPermission(): Boolean{
        //if permission isn't granted, returns false
        if (ActivityCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        return true
    }

    private fun getAddressInfo(lat: Double, long:Double): String{
        if (lat != 0.0){
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val address = geocoder.getFromLocation(lat, long, 1)

            val country = address!!.first().countryName

            val addressInfo = country
            return addressInfo
        }
        return "Location initializing"
    }
}