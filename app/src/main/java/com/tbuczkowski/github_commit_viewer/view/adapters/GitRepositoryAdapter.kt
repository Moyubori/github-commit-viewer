package com.tbuczkowski.github_commit_viewer.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.tbuczkowski.github_commit_viewer.R
import com.tbuczkowski.github_commit_viewer.model.GitRepository

class GitRepositoryAdapter(private val context: Context, private val repositories: List<GitRepository>) : BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(index: Int, view: View?, parent: ViewGroup?): View {
        val element = inflater.inflate(R.layout.repository_list_element, parent, false)
        val repositoryHandleText: TextView = element.findViewById<TextView>(R.id.repositoryHandleText)
        val repository: GitRepository = getItem(index) as GitRepository
        repositoryHandleText.text = repository.handle
        return element
    }

    override fun getItem(index: Int): Any {
        return repositories[index]
    }

    override fun getItemId(index: Int): Long {
        return index.toLong()
    }

    override fun getCount(): Int {
        return repositories.size
    }

}