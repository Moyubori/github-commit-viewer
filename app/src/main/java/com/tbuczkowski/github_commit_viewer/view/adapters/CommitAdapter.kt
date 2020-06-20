package com.tbuczkowski.github_commit_viewer.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.tbuczkowski.github_commit_viewer.R
import com.tbuczkowski.github_commit_viewer.model.Commit
import com.tbuczkowski.github_commit_viewer.model.GitRepository
import org.w3c.dom.Text

class CommitAdapter(private val context: Context, private val commits: List<Commit>) : BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(index: Int, view: View?, parent: ViewGroup?): View {
        val element: View = inflater.inflate(R.layout.commit_list_element, parent, false)
        val commitMessageTextView: TextView = element.findViewById(R.id.commitMessageText)
        val authorTextView: TextView = element.findViewById(R.id.authorText)
        val shaTextView: TextView = element.findViewById(R.id.shaText)

        val commit: Commit = getItem(index) as Commit
        commitMessageTextView.text = commit.message
        authorTextView.text = commit.author
        shaTextView.text = commit.sha

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