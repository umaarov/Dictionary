package uz.umarov.translateapp.presentation.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.umarov.translateapp.presentation.adapters.DictionaryUzbAdapter
import uz.umarov.translateapp.presentation.viewmodels.DictionaryDataViewModel
import uz.umarov.translateapp.utils.BaseUtils.showSnackToast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import uz.umarov.translateapp.R
import uz.umarov.translateapp.data.models.DictionaryModel
import uz.umarov.translateapp.databinding.FragmentUzbEngBinding
import uz.umarov.translateapp.domain.resource.DictionaryStateUI

class UzbEngFragment : Fragment(R.layout.fragment_uzb_eng) {
    private var _binding: FragmentUzbEngBinding? = null
    private lateinit var binding: FragmentUzbEngBinding

    private val viewModel by activityViewModel<DictionaryDataViewModel>()
    private val dictionaryUzbAdapter by lazy { DictionaryUzbAdapter() }
    private val navigation by lazy { findNavController() }
    private var list = emptyList<DictionaryModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUzbEngBinding.bind(view)
        _binding = binding

        initStateFlow()
        initLoadData()
        setupRecycler()
        initClicks()

    }

    private fun initLoadData() = viewModel.loadDictionaryData()

    private fun initStateFlow() = viewModel.dictionaryData.onEach { state ->
        when (state) {
            is DictionaryStateUI.Success -> {
                val sortedData = state.dictionaryData.sortedBy { it.uzbek }
                list = state.dictionaryData
                dictionaryUzbAdapter.baseList = sortedData
                showProgress(false)
            }

            is DictionaryStateUI.Loading -> showProgress(true)

            is DictionaryStateUI.Error -> {
                showProgress(false)
                showSnackToast(state.errorMessage)
            }

        }
    }.launchIn(lifecycleScope)

    private fun setupRecycler() = binding.sozlarList.apply {
        layoutManager = LinearLayoutManager(requireContext())
        setHasFixedSize(true)
        overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        adapter = dictionaryUzbAdapter
    }

    private fun initClicks() {
        dictionaryUzbAdapter.setOnStarAddClickListener {
            viewModel.toggleFavourite(it)
        }

        binding.floatingAcBtn.setOnClickListener {
            val action = BaseFragmentDirections.actionBaseFragmentToFavouritesFragment()
            navigation.navigate(action)
        }
    }

    private fun showProgress(state: Boolean) {
        binding.progressInd.isVisible = state
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}