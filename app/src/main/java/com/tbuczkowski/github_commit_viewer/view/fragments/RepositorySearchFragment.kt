package com.tbuczkowski.github_commit_viewer.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.tbuczkowski.github_commit_viewer.R
import com.tbuczkowski.github_commit_viewer.model.Commit
import com.tbuczkowski.github_commit_viewer.model.GitRepository
import com.tbuczkowski.github_commit_viewer.view.adapters.GitRepositoryAdapter

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class RepositorySearchFragment : Fragment() {

    val exampleCommits: List<Commit> = listOf(
        Commit("Merge", "1234567890", "Tymoteusz Buczkowski"),
        Commit("HelloWorld", "1234567890", "Jan Nowak"),
        Commit("Test", "1234567890", "Tymoteusz Buczkowski"),
        Commit("Initial commit", "1234567890", "Tymoteusz Buczkowski")
    )

    val exampleRepos: List<GitRepository> = listOf(
        GitRepository("moyubori/spaceshooter", "12345", exampleCommits),
        GitRepository("moyubori/mkdg", "67890", exampleCommits),
        GitRepository("moyubori/performancetester", "13579", exampleCommits)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.repository_search_fragment, container, false)

        val listView: ListView = view.findViewById<ListView>(R.id.repoHistoryListView)
        val adapter: ListAdapter =
            GitRepositoryAdapter(
                requireContext(),
                exampleRepos
            )
        listView.adapter = adapter
        listView.setOnItemClickListener { parent, view, index, id ->
            val repository: GitRepository = adapter.getItem(index) as GitRepository
            navigateToCommitListView(repository)
        }

        val repoSearchButton: Button = view.findViewById(R.id.repoSearchButton)
        repoSearchButton.setOnClickListener {
            val nameInputField: EditText = view.findViewById<EditText>(R.id.repoNameInput)
            val repoHandle: String = nameInputField.text.toString()
            // TODO: input validation
            val repository: GitRepository = fetchGitRepository(repoHandle)
            navigateToCommitListView(repository)
        }

        return view;
    }

    // TODO: fetching data from github API
    private fun fetchGitRepository(handle: String) : GitRepository {
        return GitRepository(handle, "24680", exampleCommits)
    }

    private fun navigateToCommitListView(repository: GitRepository) {
        val action = RepositorySearchFragmentDirections.actionRepositorySearchFragmentToCommitListFragment(repository)
        findNavController().navigate(action)
    }
}