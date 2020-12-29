package com.amohnacs.amiiborepo.ui.main

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout.VERTICAL
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.amohnacs.amiiborepo.R
import com.amohnacs.amiiborepo.common.InjectionFragment
import com.amohnacs.amiiborepo.common.ViewModelFactory
import com.amohnacs.amiiborepo.databinding.FragmentMainBinding
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : InjectionFragment() {

    @Inject lateinit var factory: ViewModelFactory<MainViewModel>
    @Inject lateinit var adapter: AmiiboAdapter

    private lateinit var viewModel: MainViewModel

    private var binding: FragmentMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        mainDaggerComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.recyclerView.let {
            it?.layoutManager = StaggeredGridLayoutManager(getOrientationColumnCount(), VERTICAL)
            it?.adapter = adapter
        }
//        binding?.buttonFirst?.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        viewModel.loadAmiibos()

        viewModel.amiibos.observe(viewLifecycleOwner, Observer {
            adapter.updateItems(it)
        })
        viewModel.errorEvent.observe(viewLifecycleOwner, Observer { errorString ->
            binding?.root?.let { view ->
                Snackbar.make(view, errorString, Snackbar.LENGTH_LONG).show()
            }
        })
        viewModel.emptyStateEvent.observe(viewLifecycleOwner, Observer {
            // TODO: 12/28/20 we need to replace this with an actual empty state
            binding?.root?.let { view -> Snackbar.make(view, "empty", Snackbar.LENGTH_LONG).show() }
        })
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        getOrientationColumnCount(newConfig)
    }

    private fun getOrientationColumnCount(newConfig: Configuration? = null): Int {
        return if (newConfig != null) {
            if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                LANDSCAPE_COLUMN_COUNT
            } else {
                PORTRAIT_COLUMN_COUNT
            }
        } else {
            if (newConfig?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                LANDSCAPE_COLUMN_COUNT
            } else {
                PORTRAIT_COLUMN_COUNT
            }
        }
    }

    companion object {
        const val PORTRAIT_COLUMN_COUNT = 3
        const val LANDSCAPE_COLUMN_COUNT = 5
    }
}