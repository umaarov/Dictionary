package uz.umarov.translateapp.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.umarov.translateapp.R

class ViewPagerAdapter(
    private val list: List<Fragment>,
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {

    private val fragmentIds = listOf(R.id.engUzbFragment, R.id.uzbEngFragment)

    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment = list[position]

    fun getFragmentIds(position: Int): Int = fragmentIds.indexOf(position)

}