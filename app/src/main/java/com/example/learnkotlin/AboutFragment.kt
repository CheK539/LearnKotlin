package com.example.learnkotlin

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.learnkotlin.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {
    companion object {
        private const val ARGS_NAME = "aboutPage"

        fun newInstance(): AboutFragment {
            val bundle = Bundle()
            return AboutFragment().apply { arguments = bundle }
        }
    }

    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_about, container, false
        )

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.versionText.text = "${binding.versionText.text} ${BuildConfig.VERSION_NAME}"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
}