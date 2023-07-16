package de.htw_berlin.quiz_app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.htw_berlin.quiz_app.databinding.ItemSessionBinding



class SessionAdapter(private var sessions: List<Session>) : RecyclerView.Adapter<SessionAdapter.SessionViewHolder>() {

    inner class SessionViewHolder(private val binding: ItemSessionBinding) : RecyclerView.ViewHolder(binding.root) {
        // Bind the data to the views using data binding
        fun bind(session: Session) {
                binding.session = session
                binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSessionBinding.inflate(inflater, parent, false)
        return SessionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        val session = sessions[position]
        holder.bind(session)
    }

    override fun getItemCount() = sessions.size

    // Update the sessions list when data changes
    fun updateSessions(newSessions: List<Session>) {
        sessions = newSessions
        notifyDataSetChanged()
    }
}
