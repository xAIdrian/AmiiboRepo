package com.amohnacs.amiiborepo.ui.details

import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amohnacs.amiiborepo.R
import com.amohnacs.amiiborepo.common.InjectionFragment
import com.amohnacs.amiiborepo.common.ViewModelFactory
import com.amohnacs.amiiborepo.databinding.FragmentDetailsBinding
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class DetailsFragment : InjectionFragment() {

    @Inject
    lateinit var factory: ViewModelFactory<DetailsViewModel>

    private lateinit var viewModel: DetailsViewModel

    private var binding: FragmentDetailsBinding? = null

    private val args: DetailsFragmentArgs by navArgs()
    private var amiiboTail: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        mainDaggerComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailsBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        amiiboTail = args.amiiboTail

        binding?.purchaseButton?.setOnClickListener {
            viewModel.purchaseAmiibo()
        }
        requireActivity().onBackPressedDispatcher.addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        val actions = DetailsFragmentDirections.actionDetailsFragmentToMainFragment()
                        findNavController().navigate(actions)
                    }
                }
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(DetailsViewModel::class.java)

        viewModel.amiibo.observe(viewLifecycleOwner, Observer {
            (requireActivity() as AppCompatActivity?)?.supportActionBar?.title = it.name
            binding?.root?.context?.let { context ->
                Glide.with(context)
                        .load(it.image)
                        .fitCenter()
                        .into(binding!!.image)
            }
            binding?.name?.text = it.name
            binding?.character?.text = it.character
            binding?.series?.text = it.amiiboSeries
            binding?.game?.text = it.gameSeries
            binding?.type?.text = it.type
            if (it.isPurchased == true) {
                binding?.purchaseButton?.setBackgroundColor(ContextCompat.getColor(requireActivity(), android.R.color.holo_green_dark))
                binding?.purchaseButton?.text = getString(R.string.purchased)
            }
        })
        viewModel.amiiboPurchasedState.observe(viewLifecycleOwner, Observer {
            binding?.purchaseButton?.apply {
                // with more effort we can use a <selector> and states for this
                if (it) {
                    binding?.purchaseButton?.setOnClickListener(null)
                    this.setBackgroundColor(ContextCompat.getColor(requireActivity(), android.R.color.holo_green_dark))
                    this.text = getString(R.string.purchased)
                }
            }
        })

        if (amiiboTail != null) {
            viewModel.getAmiiboDetails(amiiboTail!!)
        } else {
            binding?.root?.let { view ->
                Snackbar.make(
                        view,
                        "We do not have the Amiibo ID! Go back and try again.",
                        Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }
}