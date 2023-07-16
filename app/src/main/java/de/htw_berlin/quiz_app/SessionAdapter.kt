package de.htw_berlin.quiz_app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.htw_berlin.quiz_app.databinding.ItemSessionBinding
import java.text.SimpleDateFormat
import java.util.Locale


class SessionAdapter(private var sessions: List<Session>) : RecyclerView.Adapter<SessionAdapter.SessionViewHolder>() {

    inner class SessionViewHolder(private val binding: ItemSessionBinding) : RecyclerView.ViewHolder(binding.root) {
        // Bind the data to the views using data binding
        fun bind(formattedDate: String, formattedCorrectCount: String, formattedMode: String, formattedCompleteStatus: String, formattedCategory: String) {
            binding.apply {
                sessionDate.text = formattedDate
                correctCountSession.text = formattedCorrectCount
                modeSession.text = formattedMode
                completeSession.text = formattedCompleteStatus
                categorySession.text = formattedCategory
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSessionBinding.inflate(inflater, parent, false)
        return SessionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        val session = sessions[position]

        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val formattedDate = holder.itemView.context.getString(R.string.session_date, dateFormat.format(session.Started_At.toDate()))
        val formattedCorrectCount = holder.itemView.context.getString(R.string.correct_count, session.Count_Correct ?: 0)
        val formattedMode = holder.itemView.context.getString(R.string.mode, session.Mode ?: "")
        val formattedCompleteStatus = holder.itemView.context.getString(R.string.complete_status, if (session.isComplete == true) "Complete" else "Incomplete")
        val formattedCategory = holder.itemView.context.getString(R.string.category, session.Category ?: "")
        holder.bind(formattedDate, formattedCorrectCount, formattedMode, formattedCompleteStatus, formattedCategory)
    }

    override fun getItemCount() = sessions.size

    // Update the sessions list when data changes
    fun updateSessions(newSessions: List<Session>) {
        sessions = newSessions
        notifyDataSetChanged()
    }
}
