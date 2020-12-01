package com.example.factslistapplication.facts.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.factslistapplication.R
import com.example.factslistapplication.common.BaseFragment
import com.example.factslistapplication.facts.viewModel.FactsViewModel
import com.example.factslistapplication.facts.viewModel.FactsViewModelFactory
import kotlinx.android.synthetic.main.fragment_facts.*

class FactsListFragment : BaseFragment(R.layout.fragment_facts) {

    private lateinit var factsAdapter: FactsItemAdapter
    private val factsViewModel: FactsViewModel by viewModels { FactsViewModelFactory() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factsAdapter = FactsItemAdapter()
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
        factsViewModel.factsLiveData.observe(viewLifecycleOwner, Observer {
            factsAdapter.setItems(it)
        })
        factsViewModel.errorLiveData.observe(viewLifecycleOwner, {
           showError(it.exception.message)
        })
        factsViewModel.progressLiveData.observe(viewLifecycleOwner, Observer {
            if(it){
                indeterminateBar.visibility = View.VISIBLE
            }else{
                indeterminateBar.visibility = View.GONE
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (factsAdapter.itemCount == 0 || swipeToRefreshList.isRefreshing) {
            factsViewModel.fetchLatestData()
        }
    }

}