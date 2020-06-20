package com.tbuczkowski.github_commit_viewer.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.tbuczkowski.github_commit_viewer.R
import com.tbuczkowski.github_commit_viewer.model.Commit
import com.tbuczkowski.github_commit_viewer.model.GitRepository
import com.tbuczkowski.github_commit_viewer.view.adapters.GitRepositoryAdapter

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
        val view: View = inflater.inflate(R.layout.repository_search_fragment, container, false)

        val listView: ListView = view.findViewById<ListView>(R.id.repoHistoryList)
        val adapter: ListAdapter =
            GitRepositoryAdapter(
                requireContext(),
                exampleRepos
            )
        listView.adapter = adapter

        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.repoSearchButton).setOnClickListener {
            val nameInputField: EditText = view.findViewById<EditText>(R.id.repoNameInput)
            val repoHandle: String = nameInputField.text.toString()
            // TODO: input validation
            // TODO: fetching data from github API
            val repository: GitRepository = fetchGitRepository(repoHandle)
            navigateToCommitListView(repository)
        }
    }

    // TODO
    private fun fetchGitRepository(handle: String) : GitRepository {
        return GitRepository(handle, listOf(
            Commit("Merge", "1234567890", "Tymoteusz Buczkowski"),
            Commit("HelloWorld", "1234567890", "Jan Nowak"),
            Commit("Test", "1234567890", "Tymoteusz Buczkowski"),
            Commit("Initial commit", "1234567890", "Tymoteusz Buczkowski")
        ))
    }

    private fun navigateToCommitListView(repository: GitRepository) {
            findNavController().navigate(R.id.action_RepositorySearchFragment_to_CommitListFragment)
    }
}