package uz.umarov.translateapp.presentation.screens

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import uz.umarov.translateapp.presentation.adapters.ViewPagerAdapter
import uz.umarov.translateapp.presentation.viewmodels.ThemeViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.umarov.translateapp.R
import uz.umarov.translateapp.databinding.FragmentBaseBinding
import uz.umarov.translateapp.databinding.MainBaseViewLyBinding

class BaseFragment : Fragment(R.layout.fragment_base) {
    private var _binding: FragmentBaseBinding? = null
    private lateinit var binding: FragmentBaseBinding
    private lateinit var mainViewBinding: MainBaseViewLyBinding

    private lateinit var navController: NavController
    private val viewModel by viewModel<ThemeViewModel>()
    private val navigation by lazy { findNavController() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBaseBinding.bind(view)
        mainViewBinding = binding.fragmentsView
        _binding = binding

        setNavigationComponent()
        setupDrawerLy()
        setupViewPager()
        changeThemeViewModel()
        initClicks()

    }

    private fun changeThemeViewModel() = viewModel.getTheme.observe(viewLifecycleOwner) { isDark ->
        changeTheme(isDark)
    }

    private fun setNavigationComponent() {
        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment

        navController = navHostFragment.navController
    }

    private fun initClicks() {
        val headerView = binding.menuNavigationView.getHeaderView(0)
        val darkLightBtn = headerView.findViewById<ImageView>(R.id.light_dark_btn)

        darkLightBtn.setOnClickListener {
            viewModel.setTheme(viewModel.getTheme.value?.not() ?: false)
        }

        mainViewBinding.searchBtn.setOnClickListener {

            SearchBottomSheet().show(
                childFragmentManager,
                SearchBottomSheet.TAG
            )
        }

        binding.menuNavigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.favouritesFragment -> {
                    navigation.navigate(R.id.favouritesFragment)
                    true
                }

                else -> false
            }
        }

    }

    private fun changeTheme(themeState: Boolean) = when (themeState) {
        true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun setupViewPager() {
        val fragmentList = listOf(EngUzbFragment(), UzbEngFragment())
        val tabItems = arrayOf("English-Uzbek", "Uzbek-English")
        val tabLayout = mainViewBinding.tabLayout
        val viewPager = mainViewBinding.viewPager

        val adapter = ViewPagerAdapter(
            fragmentList,
            childFragmentManager,
            lifecycle
        )

        mainViewBinding.viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
            tab.text = tabItems[pos]
        }.attach()

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val position = adapter.getFragmentIds(destination.id)
            if (position != -1) {
                viewPager.currentItem = position
            }
        }

    }

    private fun setupDrawerLy() {
        mainViewBinding.toolbar.setNavigationOnClickListener {
            binding.drawerLy.open()
        }

        binding.menuNavigationView.setNavigationItemSelectedListener {
            it.isChecked = true
            binding.drawerLy.close()
            true
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}