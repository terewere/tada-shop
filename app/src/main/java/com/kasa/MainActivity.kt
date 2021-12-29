package com.kasa

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log.i
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.paging.ExperimentalPagingApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.kasa.databinding.ActivityMainBinding
import com.kasa.products.ProductViewModel
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.os.StrictMode
import android.os.StrictMode.VmPolicy


//pkg: com.susumate -- hash: XKK0L9NjbFh

@ExperimentalPagingApi
class MainActivity : AppCompatActivity(), HasSupportFragmentInjector,
    NavigationBarView.OnItemSelectedListener {


    private lateinit var viewModel: ProductViewModel


    private val appBarConfiguration by lazy {
        AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_categories, R.id.nav_categories
            )
        )
    }

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory



    private val navController by lazy { findNavController(R.id.nav_host_fragment) }

    private var _binding: ActivityMainBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        Handler(Looper.getMainLooper()).postDelayed({
            view.setBackgroundResource(R.color.white)
        }, 200)


        setupViewModel()
        setupUI()


//        lifecycleScope.launch {
//            viewModel.getProducts(1).distinctUntilChanged().collectLatest {
//                 i("testing", it.toString())
//               // listAdapter.submitData(it)
//
//
//            }
//        }


    }

    private fun setupViewModel() {
        try {
            val viewModelProvider = ViewModelProvider(
                navController.getViewModelStoreOwner(R.id.mobile_navigation),
                viewModelFactory
            )
            viewModel = viewModelProvider.get(ProductViewModel::class.java)
        } catch (e: IllegalArgumentException) {
            //e.printStackTrace()

            i("testing", e.localizedMessage)
        }

    }

    private fun setupBottomNavMenu() {
        binding.bottomNavView.let { bottomNavView ->
            NavigationUI.setupWithNavController(bottomNavView, navController)
        }

        binding.bottomNavView.setOnItemSelectedListener(this)
        navVisibility()

    }


    private fun setupUI() {

        setupBottomNavMenu()

        setupActionBarWithNavController(navController, appBarConfiguration)

    }



    private fun showBottomNav() {
        binding.bottomNavView.visibility = View.VISIBLE

    }

    private fun hideBottomNav() {
        binding.bottomNavView.visibility = View.GONE

    }


    private fun hideSearchView() {
        binding.searchView.visibility = View.GONE

    }

    private fun showSearchView() {
        binding.searchView.visibility = View.VISIBLE

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val retValue = super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.cart_menu, menu)

        return retValue
    }

    override fun supportNavigateUpTo(upIntent: Intent) {
        navController.navigateUp()
    }


    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> navController.navigate(R.id.nav_home)
            R.id.nav_categories -> navController.navigate(R.id.nav_categories)
//            R.id.nav_help -> navController.navigate(R.id.nav_help)
//            R.id.nav_radio -> {
//                startActivity(Intent(this, MainActivity::class.java))
//                finish()
//            }
        }
        return true
    }

    private fun navVisibility() {

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_home,  R.id.nav_categories,R.id.nav_products  -> {
                    showSearchView()
                    showBottomNav()
                }

                else -> {
                    hideBottomNav()
                    hideSearchView()
                }
            }
        }
    }

}
