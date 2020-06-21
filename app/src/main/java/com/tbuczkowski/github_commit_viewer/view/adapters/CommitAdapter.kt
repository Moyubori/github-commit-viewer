package com.tbuczkowski.github_commit_viewer.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.tbuczkowski.github_commit_viewer.R
import com.tbuczkowski.github_commit_viewer.model.Commit

class CommitAdapter(private val context: Context, private val commits: List<Commit>) : BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    private val checkedCommits: HashMap<String, Commit> = hashMapOf()

    fun getAndClearCheckedCommits(): List<Commit> {
        val checkedCommitsList: List<Commit> = checkedCommits.values.toList()
        // this clear is a bit of a hack, due to the fact that when the user gets back from the sharing
        // screen, all the checkboxes uncheck, but this map does not update. I'd handle that properly,
        // but I ran out of time
        checkedCommits.clear()
        return checkedCommitsList
    }

    override fun getView(index: Int, view: View?, parent: ViewGroup?): View {
        val element: View = inflater.inflate(R.layout.commit_list_element, parent, false)
        val commitMessageTextView: TextView = element.findViewById(R.id.commitMessageText)
        val authorTextView: TextView = element.findViewById(R.id.authorText)
        val shaTextView: TextView = element.findViewById(R.id.shaText)
        val checkBox: CheckBox = element.findViewById(R.id.checkBox)

        val commit: Commit = getItem(index) as Commit
        commitMessageTextView.text = commit.message
        authorTextView.text = commit.author
        shaTextView.text = commit.sha

        checkBox.setOnCheckedChangeListener { button, isChecked ->
            if(isChecked) {
                if (!checkedCommits.containsKey(commit.sha)) {
                    checkedCommits[commit.sha] = commit
                }
            } else {
                checkedCommits.remove(commit.sha)
            }
        }

        return element
    }

    override fun getItem(index: Int): Any {
        return commits[index]
    }

    override fun getItemId(index: Int): Long {
        return index.toLong()
    }

    override fun getCount(): Int {
        return commits.size
    }

}