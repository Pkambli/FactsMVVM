package com.example.factslistapplication.facts.view

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isNotEmpty
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.factslistapplication.FactsApplication
import com.example.factslistapplication.R
import com.example.factslistapplication.common.BaseFragment
import com.example.factslistapplication.common.UIState
import com.example.factslistapplication.facts.model.Row
import com.example.factslistapplication.facts.repository.FactsRepository
import com.example.factslistapplication.facts.viewModel.FactsViewModel
import com.example.factslistapplication.facts.viewModel.FactsViewModelFactory
import kotlinx.android.synthetic.main.fragment_facts.*


class FactsListFragment : BaseFragment(R.layout.fragment_facts), OnItemClickListener {

    private lateinit var factsAdapter: FactsItemAdapter

    private lateinit var factsViewModel: FactsViewModel

    private lateinit var navController: NavController
    private lateinit var navGraph: NavGraph

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val service = (requireActivity().applicationContext as FactsApplication).service
        factsViewModel =
            ViewModelProvider(this, FactsViewModelFactory(FactsRepository(service))).get(
                FactsViewModel::class.java
            )
        factsAdapter = FactsItemAdapter(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = factsAdapter
        }
        swipeToRefreshList.apply {
            isEnabled = true
            setOnRefreshListener {
                isRefreshing = true
                factsViewModel.fetchLatestData()
                isRefreshing = false
            }
        }

        factsViewModel.factsState.observe(viewLifecycleOwner, {
            when (it) {
                is UIState.Error -> showError(it.error.exception.message)
                is UIState.Success -> factsAdapter.setItems(it.data)
                is UIState.Loading -> {
                    swipeToRefreshList.isRefreshing = it.isLoading
                }
            }
        })
    }

    override fun onItemClick(row: Row) {
        val activity = requireActivity()
        val navController = Navigation.findNavController(activity, R.id.mainNavigationFragment)
        val bundle = bundleOf(HomeActivity.EXTRA_ROW_DATA to row.imageHref)
        navController.navigate(R.id.factsDetailsFragment, bundle)

    }


}