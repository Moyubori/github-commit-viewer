package com.tbuczkowski.github_commit_viewer.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.tbuczkowski.github_commit_viewer.R
import com.tbuczkowski.github_commit_viewer.model.Commit
import com.tbuczkowski.github_commit_viewer.model.GitRepository
import com.tbuczkowski.github_commit_viewer.view.adapters.CommitAdapter

class CommitListFragment : Fragment() {

    private val args: CommitListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.commit_list_fragment, container, false)

        val repository: GitRepository = args.repository
        val commits: List<Commit> = repository.commits

        val listView: ListView = view.findViewById<ListView>(R.id.commitsListView)
        val adapter: ListAdapter = CommitAdapter(requireContext(), commits)
        listView.adapter = adapter

        val repositoryIdTextView: TextView = view.findViewById<TextView>(R.id.repositoryIdText)
        val formattedRepositoryIdText: String = String.format(resources.getString(R.string.commit_list_repository_id), repository.id)
        repositoryIdTextView.text = formattedRepositoryIdText

        return view
    }

}