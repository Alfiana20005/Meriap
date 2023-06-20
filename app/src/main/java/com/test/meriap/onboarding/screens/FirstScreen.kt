package com.test.meriap.onboarding.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.test.meriap.R
import androidx.viewpager2.widget.ViewPager2
//import kotlinx.android.synthetic.main.fragment_first_screen.view.*
import com.test.meriap.databinding.FragmentFirstScreenBinding

class FirstScreen : Fragment() {
    private var _binding: FragmentFirstScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var tvSkip: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        val view = inflater.inflate(R.layout.fragment_first_screen, container, false)
//
//        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
//
//        view.next.setOnClickListener{
//            viewPager?.currentItem = 1
//        }
        _binding = FragmentFirstScreenBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.next.setOnClickListener {
            val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
            viewPager?.currentItem = 1
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}