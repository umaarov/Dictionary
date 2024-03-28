package uz.umarov.translateapp.presentation.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.umarov.translateapp.presentation.adapters.FavouritesAdapter
import uz.umarov.translateapp.presentation.viewmodels.FavouritesViewModel
import uz.umarov.translateapp.utils.BaseUtils.showSnackToast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.umarov.translateapp.R
import uz.umarov.translateapp.databinding.FragmentFavouritesBinding
import kotlin.properties.Delegates

class FavouritesFragment : Fragment(R.layout.fragment_favourites) {
    private var _binding: FragmentFavouritesBinding? = null
    private lateinit var binding: FragmentFavouritesBinding

    private val viewModel by viewModel<FavouritesViewModel>()
    private val favouritesAdapter by lazy { FavouritesAdapter() }
    private val navigation by lazy { findNavController() }
    private var numberOfFavourites by Delegates.notNull<Int>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavouritesBinding.bind(view)
        _binding = binding

        initLiveData()
        viewModel.getAllFavourites()
        initClicks()
        setupRecycler()

    }

    private fun initLiveData() = viewModel.run {

        favouriteWords.onEach { favouritesAdapter.submitList(it) }.launchIn(lifecycleScope)

        countedData.observe(viewLifecycleOwner) { value ->
            numberOfFavourites = value
            if (value == 0) binding.apply {
                toolbar.title = "Favourites"
                emptyTxt.isVisible = true
            }
            else binding.apply {
                toolbar.title = "Favourites - $value"
                emptyTxt.isVisible = false
            }
        }

    }

    private fun initClicks() {
        binding.toolbar.setNavigationOnClickListener {
            val action = FavouritesFragmentDirections.actionFavouritesFragmentToBaseFragment()
            navigation.navigate(action)
        }

        favouritesAdapter.setOnDeleteClickListener {
            viewModel.deleteFavWord(it.id)
        }

    }

    private fun setupRecycler() = binding.favouriteWordsList.apply {
        overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(requireContext())
        adapter = favouritesAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}