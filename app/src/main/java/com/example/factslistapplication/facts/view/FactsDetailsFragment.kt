package com.example.factslistapplication.facts.view

import android.os.Bundle
import android.view.View
import com.example.factslistapplication.R
import com.example.factslistapplication.common.BaseFragment
import com.example.factslistapplication.utils.imageLoading
import com.example.factslistapplication.facts.view.HomeActivity.Companion.EXTRA_ROW_DATA
import kotlinx.android.synthetic.main.fragment_facts_details.*

class FactsDetailsFragment : BaseFragment(R.layout.fragment_facts_details) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rowArguments = arguments?.getString(EXTRA_ROW_DATA)
        imageViewDetails.imageLoading(rowArguments ?: "")
    }
}