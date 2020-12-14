package com.example.factslistapplication.common

import android.widget.Toast
import androidx.fragment.app.Fragment

open class BaseFragment(id: Int) : Fragment(id) {

    fun showError(message: String?) {
        Toast.makeText(requireContext(), message.orEmpty(), Toast.LENGTH_LONG).show()
    }


}