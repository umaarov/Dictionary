package uz.umarov.translateapp.presentation.screens

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.umarov.translateapp.presentation.adapters.DictionaryEngAdapter
import uz.umarov.translateapp.presentation.viewmodels.DictionaryDataViewModel
import uz.umarov.translateapp.utils.BaseUtils.showSnackToast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import uz.umarov.translateapp.R
import uz.umarov.translateapp.databinding.FragmentEngUzbBinding
import uz.umarov.translateapp.domain.resource.DictionaryStateUI

class EngUzbFragment : Fragment(R.layout.fragment_eng_uzb) {
    private var _binding: FragmentEngUzbBinding? = null
    private lateinit var binding: FragmentEngUzbBinding

    private val viewModel by activityViewModel<DictionaryDataViewModel>()
    private val dictionaryEngAdapter by lazy { DictionaryEngAdapter() }
    private val navigation by lazy { findNavController() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEngUzbBinding.bind(view)
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
                dictionaryEngAdapter.baseList = state.dictionaryData
                showProgress(false)
            }

            is DictionaryStateUI.Loading -> showProgress(true)

            is DictionaryStateUI.Error -> {
                showProgress(false)
                showSnackToast(state.errorMessage)
            }

        }
    }.launchIn(lifecycleScope)

    private fun setupRecycler() = binding.lettersList.apply {
        overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(requireContext())
        adapter = dictionaryEngAdapter
    }

    private fun initClicks() {

        dictionaryEngAdapter.setOnStarClickListener {
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