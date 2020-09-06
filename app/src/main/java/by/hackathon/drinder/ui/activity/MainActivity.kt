package by.hackathon.drinder.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import by.hackathon.drinder.App
import by.hackathon.drinder.R
import by.hackathon.drinder.di.ViewModelFactory
import by.hackathon.drinder.util.isLocationPermissionGranted
import by.hackathon.drinder.util.requestLocationPermission
import javax.inject.Inject

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)

        if (!isLocationPermissionGranted()) requestLocationPermission()

        setupActionBarWithNavController(findNavController(R.id.nav_host_fragment))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_change_theme, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.change_theme) {
            viewModel.notifyThemeChanged()
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp()
    }
}
