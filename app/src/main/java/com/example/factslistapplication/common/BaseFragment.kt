package com.example.factslistapplication.common

import android.app.Application
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.factslistapplication.FactsApplication

open class BaseFragment(id: Int) : Fragment(id) {

    fun showError(message: String?) {
        Toast.makeText(requireContext(), message.orEmpty(), Toast.LENGTH_LONG).show()
    }


}