package com.example.factslistapplication.facts.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.factslistapplication.R
import com.example.factslistapplication.common.BaseFragment
import com.example.factslistapplication.common.UIState
import com.example.factslistapplication.facts.model.Row
import com.example.factslistapplication.facts.viewModel.FactsViewModel
import com.example.factslistapplication.facts.viewModel.FactsViewModelFactory
import kotlinx.android.synthetic.main.fragment_facts.*


class FactsListFragment : BaseFragment(R.layout.fragment_facts), OnItemClickListener {

    private lateinit var factsAdapter: FactsItemAdapter
    private val factsViewModel: FactsViewModel by viewModels { FactsViewModelFactory() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factsAdapter = FactsItemAdapter(this)
        factRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = factsAdapter
        }
        swipeToRefreshList.apply {
            isEnabled = true
            setOnRefreshListener {
                factsViewModel.fetchLatestData()
                isRefreshing = false
            }
        }

        factsViewModel.factsState.observe(viewLifecycleOwner, {
            when (it) {
                is UIState.Error -> showError(it.error.exception.message)
                is UIState.Success -> factsAdapter.setItems(it.data)
                is UIState.Loading -> {
                    if (it.isLoading && !swipeToRefreshList.isRefreshing) {
                        indeterminateBar.visibility = View.VISIBLE
                    } else {
                        indeterminateBar.visibility = View.GONE
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (factsAdapter.itemCount == 0 || swipeToRefreshList.isRefreshing) {
            factsViewModel.fetchLatestData()
        }
    }

    override fun onItemClick(row: Row) {
        val activity = requireActivity()
        if (activity is OnItemClickListener) {
            activity.onItemClick(row)
        }
    }


}