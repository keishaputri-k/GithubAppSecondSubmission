package com.kei.githubappsecondsubmission.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kei.githubappsecondsubmission.databinding.ActivityMainBinding
import com.kei.githubappsecondsubmission.domainNetwork.data.model.UsersItem
import com.kei.githubappsecondsubmission.ui.detail.dashboard.DetailActivity
import dagger.hilt.android.AndroidEntryPoint

//guna AndroidEntryPoint karena kita pakai injector
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        setViewModelProvider()
        loading()
        showRecycler()
        observeData()
        getSearchData()
    }

    private fun getSearchData() {
        mainBinding.svMain.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.getSearchUser(query)
                try {
                    val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(mainBinding.root.windowToken, 0)
                }catch (e : Throwable){
                    Log.v("DEBUG", e.toString())
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                mainViewModel.getSearchUser(newText)
                return false
            }
        })
    }

    private fun observeData() {
        mainViewModel.listUserLiveData.observe(this, { user ->
            if ((user?.size ?: 0) == 0 ){
                mainBinding.apply {
                    ivErrorMain.visibility = View.VISIBLE
                    rvMain.visibility = View.GONE
                }
            }else{
                mainBinding.apply {
                    ivErrorMain.visibility = View.GONE
                    rvMain.visibility = View.VISIBLE
                    val mainAdapter = MainAdapter(user)
                    rvMain.adapter = mainAdapter
                    //kirim data ke detail
                    val intent = Intent(this@MainActivity, DetailActivity::class.java)
                    mainAdapter.setItemClickCallback(object : OnItemClickCallback {
                        override fun onItemClicked(user: UsersItem?) {
                            intent.putExtra(DetailActivity.EXTRA_DATA, user)
                            startActivity(intent)
                        }

                    })
                }
            }
        })
    }
    private fun loading() {
        mainViewModel.loading.observe(this, {isLoading ->
            if (isLoading){
                mainBinding.pbMain.visibility = View.VISIBLE
            }else{
                mainBinding.pbMain.visibility = View.GONE
            }
        })
    }
    private fun showRecycler() {
        mainBinding.rvMain.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = MainAdapter(listOf())
        }
    }

    private fun setViewModelProvider() {
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }
}