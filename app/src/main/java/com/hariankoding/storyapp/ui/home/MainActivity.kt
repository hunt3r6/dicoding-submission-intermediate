package com.hariankoding.storyapp.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hariankoding.storyapp.data.adapter.LoadingStateAdapter
import com.hariankoding.storyapp.databinding.ActivityMainBinding
import com.hariankoding.storyapp.ui.createstory.CreateStoryActivity
import com.hariankoding.storyapp.ui.login.LoginActivity
import com.hariankoding.storyapp.utils.SharedPreferencesHelper
import com.hariankoding.storyapp.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val storyAdapter by lazy {
        StoryAdapter()
    }

    private val prefHelper by lazy {
        SharedPreferencesHelper.invoke(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setView()
        setObserver()
        setClick()
    }

    private fun setClick() {
        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Logout")
                setMessage("Apakah Anda ingin keluar?")
                setPositiveButton("Iya") { _, _ ->
                    prefHelper.deleteDataAuth()
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish()
                }
                create()
                show()
            }
        }

        binding.btnAddStory.setOnClickListener {
            launcherIntent.launch(Intent(this@MainActivity, CreateStoryActivity::class.java))
        }

    }

    private val launcherIntent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == CREATE_STORY) {
                val isUpdate = it.data?.getBooleanExtra("isUpdate", false)
                if (isUpdate == true) {
                    storyAdapter.refresh()
                }
            }
        }

    private fun setObserver() {
        viewModel.listStoryResponse.observe(this) {
            storyAdapter.submitData(lifecycle, it)
        }
    }

    private fun setView() = with(binding) {
        rvStoryList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = storyAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    storyAdapter.retry()
                }
            )
        }
    }

    companion object {
        const val CREATE_STORY = 200
    }
}