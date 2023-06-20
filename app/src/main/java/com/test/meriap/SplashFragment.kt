package com.test.meriap

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val delayTime = 2000L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        Handler().postDelayed({
//            findNavController().navigate(R.id.action_splashFragment_to_viewPagerFragment)
//        }, 2000)

        coroutineScope.launch {
            delay(delayTime)
            findNavController().navigate(R.id.action_splashFragment_to_viewPagerFragment)
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

}