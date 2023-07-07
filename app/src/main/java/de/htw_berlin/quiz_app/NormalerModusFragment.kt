package de.htw_berlin.quiz_app

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class NormalerModusFragment : Fragment() {

    companion object {
        fun newInstance() = NormalerModusFragment()
    }

    private lateinit var viewModel: NormalerModusViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_normaler_modus, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NormalerModusViewModel::class.java)
        // TODO: Use the ViewModel
    }

}