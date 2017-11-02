package com.alessandrogaboardi.instatest.fragments

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alessandrogaboardi.instatest.R

/**
 * A placeholder fragment containing a simple view.
 */
class FragmentHome : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_activity_home, container, false)
    }
}
