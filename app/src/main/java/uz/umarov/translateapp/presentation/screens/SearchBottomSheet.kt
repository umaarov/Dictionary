package uz.umarov.translateapp.presentation.screens

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.umarov.translateapp.presentation.adapters.DictionaryEngAdapter
import uz.umarov.translateapp.presentation.viewmodels.DictionaryDataViewModel
import uz.umarov.translateapp.utils.BaseUtils.showSnackToast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import uz.umarov.translateapp.data.models.DictionaryModel
import uz.umarov.translateapp.databinding.FragmentSearchBottomSheetListDialogBinding
import uz.umarov.translateapp.domain.resource.DictionaryStateUI
import java.util.Locale

class SearchBottomSheet : BottomSheetDialogFragment() {
    private var _binding: FragmentSearchBottomSheetListDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModel<DictionaryDataViewModel>()
    private val dictionaryEngAdapter by lazy { DictionaryEngAdapter() }
    private var list: List<DictionaryModel> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBottomSheetListDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initStateFlow()
        loadData()
        setupRecycler()
        setupSearch()

        binding.backBtn.setOnClickListener {
            dismiss()
        }

        dictionaryEngAdapter.setOnStarClickListener {
            viewModel.toggleFavourite(it)
        }

    }

    private fun loadData() = viewModel.loadDictionaryData()

    private fun initStateFlow() = viewModel.dictionaryData.onEach { state ->
        when (state) {
            is DictionaryStateUI.Success -> {
                state.dictionaryData.also {
                    dictionaryEngAdapter.baseList = it
                    list = it
                    Log.d(TAG, "initStateFlow: $it")
                }
                showProgress(false)
            }

            is DictionaryStateUI.Loading -> showProgress(true)

            is DictionaryStateUI.Error -> {
                showSnackToast(state.errorMessage)
                showProgress(false)
            }

        }

    }.launchIn(lifecycleScope)

    private fun setupRecycler() = binding.lettersList.apply {
        overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(requireContext())
        adapter = dictionaryEngAdapter
    }

    private fun setupSearch() {
        val queryTextListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                val text = newText?.trim()?.lowercase()!!
                if (text.isNotEmpty()) {
                    val filterResult = list.filter { words ->
                        words.english.trim().lowercase(Locale.getDefault())
                            .contains(text) or words.uzbek.trim().lowercase(Locale.getDefault())
                            .contains(text)
                    }
                    dictionaryEngAdapter.baseList = filterResult
                } else {
                    dictionaryEngAdapter.baseList = list
                }
                return false
            }
        }
        binding.searchView.setOnQueryTextListener(queryTextListener)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener { sheet ->
            val bottomSheetDialog = sheet as BottomSheetDialog
            val bottomSheet =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.isDraggable = true
                behavior.isFitToContents = true
                behavior.skipCollapsed = true
                behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                            dismiss()
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                })
            }
        }
        return dialog
    }

    private fun showProgress(state: Boolean) {
        binding.linearProgress.isVisible = state
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}