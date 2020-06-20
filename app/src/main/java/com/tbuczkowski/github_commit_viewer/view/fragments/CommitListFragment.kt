package com.tbuczkowski.github_commit_viewer.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.ListView
import com.tbuczkowski.github_commit_viewer.R
import com.tbuczkowski.github_commit_viewer.model.Commit
import com.tbuczkowski.github_commit_viewer.view.adapters.CommitAdapter

class CommitListFragment : Fragment() {

    val exampleCommits: List<Commit> = listOf(
        Commit("Merge", "1234567890", "Tymoteusz Buczkowski"),
        Commit("HelloWorld", "1234567890", "Jan Nowak"),
        Commit("Test", "1234567890", "Tymoteusz Buczkowski"),
        Commit("Initial commit", "1234567890", "Tymoteusz Buczkowski")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.commit_list_fragment, container, false)

        val listView: ListView = view.findViewById<ListView>(R.id.commitsListView)
        val adapter: ListAdapter = CommitAdapter(requireContext(), exampleCommits)
        listView.adapter = adapter

        return view
    }

}