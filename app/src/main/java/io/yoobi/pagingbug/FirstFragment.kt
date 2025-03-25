package io.yoobi.pagingbug

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import io.yoobi.pagingbug.databinding.FragmentFirstBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

// Utility function to handle refreshing and scrolling to top
fun RecyclerView.scrollToTopOnInitialLoad(
    adapter: PagingDataAdapter<*, *>,
    lifecycleOwner: LifecycleOwner // Pass lifecycleOwner
) {
    var previousState: LoadState = LoadState.Loading // Initialize previous state as Loading

    adapter.loadStateFlow
        .flowWithLifecycle(lifecycleOwner.lifecycle) // Collect flow only when lifecycle is in a valid state
        .onEach { loadStates ->
            val currentState = loadStates.refresh // Track the refresh state
            // Only trigger scroll to position 0 when the state transitions from Loading to NotLoading
            if (previousState is LoadState.Loading && currentState is LoadState.NotLoading) {
                // Refresh is done, scroll to position 0 after data is loaded
                Log.e("FirstFragment", "Initial load finished. Scrolling to position 0")
                this@scrollToTopOnInitialLoad.scrollToPosition(0)
            }

            // Update previousState for the next iteration
            previousState = currentState
        }.launchIn(lifecycleOwner.lifecycleScope)
}

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: FirstFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topAdapter = TopAdapter()
        val adapter = NumberAdapter {
            findNavController().navigate(FirstFragmentDirections.actionFirstFragmentToSecondFragment(it))
        }
        binding.recyclerview.adapter = ConcatAdapter(topAdapter, adapter)
        binding.btnRefresh.setOnClickListener {
            binding.recyclerview.scrollToPosition(0)
            adapter.refresh()
        }
        binding.recyclerview.scrollToTopOnInitialLoad(adapter, this)
        lifecycleScope.launch {
            viewModel.numbers.collectLatest { pagingData ->
                Log.e("FirstFragment", "$pagingData")
                adapter.submitData(pagingData)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}