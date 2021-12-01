package com.kei.githubappsecondsubmission.adater

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kei.githubappsecondsubmission.view.detail.follower.FollowerFragment
import com.kei.githubappsecondsubmission.view.detail.following.FollowingFragment

class ViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var username: String? = null

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowerFragment.newInstance(username ?: "")
            1 -> fragment = FollowingFragment.newInstance(username ?: "")
        }
        return fragment as Fragment
    }
}