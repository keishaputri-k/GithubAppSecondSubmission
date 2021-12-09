package com.kei.githubappsecondsubmission.ui.detail.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kei.githubappsecondsubmission.R
import com.kei.githubappsecondsubmission.pagerAdater.ViewPagerAdapter
import com.kei.githubappsecondsubmission.databinding.ActivityDetailBinding
import com.kei.githubappsecondsubmission.domainNetwork.data.model.UsersItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {
    private lateinit var detailBinding : ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private var user : UsersItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)
        getListDataObject()
        setViewModelProvider()
        observeData()
        loading()
        errorSetup()
        setViewPager()
    }

    private fun setViewPager() {
        val viewPagerAdapter = ViewPagerAdapter(this)
        viewPagerAdapter.username = user?.login
        detailBinding.vpDetail.adapter = viewPagerAdapter
        val tabLayout: TabLayout = detailBinding.tlDetail
        TabLayoutMediator(tabLayout, detailBinding.vpDetail){tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun errorSetup() {
        detailViewModel.error.observe(this, {
            if (it == null){
                detailBinding.ivErrorDetail.visibility = View.GONE
                detailBinding.clDetail.visibility = View.VISIBLE
            }else{
                detailBinding.ivErrorDetail.visibility = View.VISIBLE
                detailBinding.clDetail.visibility = View.GONE
            }
        })
    }

    private fun loading() {
        detailViewModel.loading.observe(this, {isLoading ->
            if (isLoading){
                detailBinding.pbDetail.visibility = View.VISIBLE
            }else{
                detailBinding.pbDetail.visibility = View.GONE
            }
        })
    }

    private fun observeData() {
        detailViewModel.getDetailUser(user?.login ?: "")
        detailViewModel.detailUser.observe(this, {detail ->
            detailBinding.apply {
                Glide.with(this.root).load(detail?.avatar_url).apply(
                    RequestOptions().centerCrop().override(100)
                ).into(ivDetail)

                tvNameDetail.text = detail?.name ?: "NO AVAILABLE"
                tvDetailCompany.text = detail?.company ?: "NO AVAILABLE"
                tvDetailUsername.text = detail?.login ?: "NO AVAILABLE"
                tvDetailLocation.text = detail?.location ?: "NO AVAILABLE"
                tvDetailRepository.text = (detail?.public_repos?: "NO AVAILABLE").toString()
                tvDetailFollowers.text= (detail?.followers?: "NO AVAILABLE").toString()
                tvDetailFollowing.text= (detail?.following?: "NO AVAILABLE").toString()
            }
        })
    }

    private fun setViewModelProvider() {
        detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]
    }

    private fun getListDataObject() {
        user = intent.getParcelableExtra(EXTRA_DATA)
    }

    companion object{
        const val EXTRA_DATA = "extra_data"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.txt_followers,
            R.string.txt_following
        )
    }
}