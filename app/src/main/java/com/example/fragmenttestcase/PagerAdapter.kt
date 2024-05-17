package com.example.fragmenttestcase

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class PagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    private val fragmentList = mutableListOf<PageFragment>()

    override fun getItemCount(): Int = fragmentList.size

    init {
        fragmentList.add(PageFragment.newInstance(1))
    }

    fun submitList(count: Int) {
        fragmentList.removeAll(fragmentList)
        for (i in 1..count) {
            fragmentList.add(PageFragment.newInstance(i))
        }
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun addFragment() {
        fragmentList.add(PageFragment.newInstance(fragmentList.size + 1))
        notifyItemInserted(fragmentList.size - 1)
    }

    fun removeFragment() {
        if (fragmentList.size > 1) {
            fragmentList.removeAt(fragmentList.size - 1)
            notifyItemRemoved(fragmentList.size)
        }
    }
}