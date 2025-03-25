package io.yoobi.pagingbug

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import io.yoobi.pagingbug.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// Utility function to handle refreshing and scrolling to top
fun RecyclerView.scrollToTopOnInitialLoad(
    adapter: PagingDataAdapter<*, *>,
    lifecycleOwner: LifecycleOwner // Pass lifecycleOwner
) {
    var previousState: LoadState = LoadState.Loading // Initialize previous state as Loading

    lifecycleOwner.lifecycleScope.launch {
        adapter.loadStateFlow
            .flowWithLifecycle(lifecycleOwner.lifecycle) // Collect flow only when lifecycle is in a valid state
            .collect { loadStates ->
                val currentState = loadStates.refresh // Track the refresh state

                // Only trigger scroll to position 0 when the state transitions from Loading to NotLoading
                if (previousState is LoadState.Loading && currentState is LoadState.NotLoading) {
                    // Refresh is done, scroll to position 0 after data is loaded
                    Log.e("MainActivity", "Initial load finished. Scrolling to position 0.")
                    this@scrollToTopOnInitialLoad.scrollToPosition(0)
                }

                // Update previousState for the next iteration
                previousState = currentState
            }
    }
}

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
            adapter.refresh()
        }
        binding.recyclerview.scrollToTopOnInitialLoad(adapter, this)
        lifecycleScope.launch {
            viewModel.numbers.collectLatest { pagingData ->
                Log.e("MainActivity", "$pagingData")
                adapter.submitData(pagingData)
            }
        }
    }
}
