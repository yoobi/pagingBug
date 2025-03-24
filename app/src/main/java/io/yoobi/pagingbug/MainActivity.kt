package io.yoobi.pagingbug

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.yoobi.pagingbug.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()
    private val adapter = NumberAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerview.adapter = adapter
        binding.btnRefresh.setOnClickListener {
            Log.e("MainActivity", "refresh")
            adapter.refresh()
        }
        lifecycleScope.launch {
            viewModel.numbers.collectLatest { pagingData ->
                Log.e("MainActivity", "$pagingData")
                adapter.submitData(pagingData)
            }
        }
    }
}
