package by.hackathon.drinder.ui.map

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import by.hackathon.drinder.R
import by.hackathon.drinder.di.ViewModelFactory
import by.hackathon.drinder.util.daggerAppComponent
import com.google.android.gms.maps.SupportMapFragment
import javax.inject.Inject

class MapFragment : Fragment(R.layout.fragment_map) {

    private val navController by lazy { findNavController() }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: MapViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        daggerAppComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        lifecycle.addObserver(viewModel)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModelObservers()
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(viewModel)
    }

    private fun setupViewModelObservers() {
        viewModel.gpsPermissionState.observe(viewLifecycleOwner) {
            if (!it) Toast.makeText(
                context,
                R.string.error_gps_permission,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_map_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.my_profile -> {
                navController.navigate(R.id.action_mapFragment_to_userDetailFragment)
                true
            }
            R.id.send_my_location -> {
                viewModel.notifyLocationSendRequest()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
