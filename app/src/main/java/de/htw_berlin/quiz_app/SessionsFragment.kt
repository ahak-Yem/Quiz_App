package de.htw_berlin.quiz_app

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import de.htw_berlin.quiz_app.databinding.FragmentSessionsBinding

class SessionsFragment : Fragment() {

    private lateinit var binding: FragmentSessionsBinding
    private lateinit var sessionAdapter: SessionAdapter
    private val viewModel: SessionsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSessionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = requireContext().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val spitzname = sharedPreferences.getString("spitzname", "") //Get userID from app preferences
        if (spitzname != null) {
            binding.username = spitzname
            viewModel.getUserSessions(spitzname, onSuccess = {
                binding.totalSessions = it.toString()
                Toast.makeText(requireContext(), "Erfolgreich", Toast.LENGTH_SHORT).show()
            }, onFailure = {
                Toast.makeText(requireContext(),"Versuchen Sie später",Toast.LENGTH_SHORT).show()
            })
        }
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        sessionAdapter = SessionAdapter(emptyList()) // Pass an empty list initially
        binding.sessionsRecyclerView.apply {
            adapter = sessionAdapter
            layoutManager = LinearLayoutManager(requireContext())
            //setHasFixedSize(false)
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.sessions.observe(viewLifecycleOwner) { sessions ->
            sessionAdapter.updateSessions(sessions)
        }
    }
}
