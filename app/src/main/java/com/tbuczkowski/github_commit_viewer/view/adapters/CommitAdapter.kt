package com.tbuczkowski.github_commit_viewer.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.tbuczkowski.github_commit_viewer.R
import com.tbuczkowski.github_commit_viewer.model.Commit

class CommitAdapter(private val context: Context, private val commits: List<Commit>) :
    RecyclerView.Adapter<CommitAdapter.CommitViewHolder>() {

    class CommitViewHolder(val view: View): RecyclerView.ViewHolder(view) {

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Long? = itemId
            }

        fun bind(isActivated: Boolean = false) {
            itemView.isActivated = isActivated
        }
    }

    class CommitItemDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {
        override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
            val view  = recyclerView.findChildViewUnder(e.x, e.y)
            if (view != null) {
                return (recyclerView.getChildViewHolder(view) as CommitAdapter.CommitViewHolder).getItemDetails()
            }
            return null
        }

    }

    var tracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommitViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.commit_list_element, parent, false)
        return CommitViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommitViewHolder, index: Int) {
        val view: View = holder.view
        val commitMessageTextView: TextView = view.findViewById(R.id.commitMessageText)
        val authorTextView: TextView = view.findViewById(R.id.authorText)
        val shaTextView: TextView = view.findViewById(R.id.shaText)

        val commit: Commit = commits[index] as Commit
        commitMessageTextView.text = commit.message
        authorTextView.text = commit.author
        shaTextView.text = commit.sha

        tracker?.let {
            holder.bind(it.isSelected(index.toLong()))
        }
    }

    override fun getItemCount(): Int {
        return commits.size
    }

    override fun getItemId(index: Int): Long {
        return index.toLong()
    }

}

