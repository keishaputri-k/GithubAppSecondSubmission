package com.kei.githubappsecondsubmission.ui.detail.following

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kei.githubappsecondsubmission.databinding.FragmentFollowingBinding
import com.kei.githubappsecondsubmission.ui.home.MainAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowingFragment : Fragment() {
    private lateinit var followingBinding: FragmentFollowingBinding
    private lateinit var followingViewModel: FollowingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        followingBinding = FragmentFollowingBinding.inflate(layoutInflater)
        setViewModelProvider()
        setRecyclerView()
        observeData()
        loading()
        setError()
        return followingBinding.root
    }

    private fun setError() {
        followingViewModel.error.observe(viewLifecycleOwner, {
            if (it == null){
                followingBinding.apply {
                    ivErrorFollowingPage.visibility = View.GONE
                    rvFollowingPage.visibility = View.VISIBLE
                }
            }else{
                followingBinding.apply {
                    ivErrorFollowingPage.visibility = View.VISIBLE
                    rvFollowingPage.visibility = View.GONE
                }
            }
        })
    }

    private fun loading() {
        followingViewModel.loading.observe(viewLifecycleOwner, { isLoading ->
            if (isLoading) {
                followingBinding.apply {
                    pbFollowingPage.visibility = View.VISIBLE
                    rvFollowingPage.visibility = View.GONE
                }
            } else {
                followingBinding.apply {
                    pbFollowingPage.visibility = View.GONE
                    rvFollowingPage.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun observeData() {
        val username = arguments?.getString(USERNAME)
        followingViewModel.getFollowing(username ?: "")
        followingViewModel.followingLiveData.observe(viewLifecycleOwner, { listFollowing ->
            if ((listFollowing?.size ?: 0) == 0) {
                followingBinding.apply {
                    ivErrorFollowingPage.visibility = View.VISIBLE
                    rvFollowingPage.visibility = View.GONE
                }
            } else {
                followingBinding.apply {
                    pbFollowingPage.visibility = View.GONE
                    rvFollowingPage.visibility = View.VISIBLE

                    val mainAdapter = MainAdapter(listFollowing)
                    rvFollowingPage.adapter = mainAdapter
                }
            }
        })
    }

    private fun setViewModelProvider() {
        followingViewModel = ViewModelProvider(this)[FollowingViewModel::class.java]
    }

    private fun setRecyclerView() {
        followingBinding.rvFollowingPage.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = MainAdapter(listOf())
        }
    }

    companion object {
        private const val USERNAME = "username"
        fun newInstance(username: String): Fragment {
            val fragment = FollowingFragment()
            val bundle = Bundle()
            bundle.putString(USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }
}