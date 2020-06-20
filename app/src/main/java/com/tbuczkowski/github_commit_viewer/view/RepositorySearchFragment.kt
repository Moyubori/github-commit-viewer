package com.tbuczkowski.github_commit_viewer.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.tbuczkowski.github_commit_viewer.R
import com.tbuczkowski.github_commit_viewer.model.GitRepository

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class RepositorySearchFragment : Fragment() {

    val exampleRepos: List<GitRepository> = listOf(
        GitRepository("moyubori/spaceshooter"),
        GitRepository("moyubori/mkdg"),
        GitRepository("moyubori/performancetester")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_first, container, false)

        val listView: ListView = view.findViewById<ListView>(R.id.repoHistoryList)
        val repoHandles: List<String> = exampleRepos.map { it.handle }
        val adapter: ListAdapter = GitRepositoryAdapter(requireContext(), exampleRepos)
        listView.adapter = adapter

        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.repoSearchButton).setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }
}