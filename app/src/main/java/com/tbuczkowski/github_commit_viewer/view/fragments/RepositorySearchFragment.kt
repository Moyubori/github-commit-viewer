package com.tbuczkowski.github_commit_viewer.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.tbuczkowski.github_commit_viewer.R
import com.tbuczkowski.github_commit_viewer.Utils
import com.tbuczkowski.github_commit_viewer.data_providers.GitRepositoryProvider
import com.tbuczkowski.github_commit_viewer.model.Commit
import com.tbuczkowski.github_commit_viewer.model.GitRepository
import com.tbuczkowski.github_commit_viewer.view.adapters.GitRepositoryAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

// I didn't bother checking the detailed GitHub user/repo name requirements, but I believe this will be good enough for a POC app
private const val repositoryHandleValidationRegex: String = "[A-Za-z-._0-9]+\\/[A-Za-z-._0-9]+"

class RepositorySearchFragment : Fragment() {

    private lateinit var gitRepositoryProvider: GitRepositoryProvider


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        gitRepositoryProvider =  GitRepositoryProvider.getInstance(requireContext())

        val view: View = inflater.inflate(R.layout.repository_search_fragment, container, false)

        val listView: ListView = view.findViewById<ListView>(R.id.repoHistoryListView)
        val adapter: ListAdapter =
            GitRepositoryAdapter(
                requireContext(),
                gitRepositoryProvider.getCachedRepositories()
            )
        listView.adapter = adapter
        listView.setOnItemClickListener { _, _, index, _ ->
            val repository: GitRepository = adapter.getItem(index) as GitRepository
            navigateToCommitListView(repository)
        }

        val repoSearchButton: Button = view.findViewById(R.id.repoSearchButton)
        repoSearchButton.setOnClickListener {
            val nameInputField: EditText = view.findViewById<EditText>(R.id.repoNameInput)
            val repoHandle: String = nameInputField.text.toString()
            if (validateInput(repoHandle)) {
                gitRepositoryProvider.fetchRepository(repoHandle)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        navigateToCommitListView(it)
                    }, {
                        val exception: HttpException = it as HttpException
                        val errorMessage: String = String.format(
                            resources.getText(R.string.error_fetching_repo).toString(),
                            exception.code()
                        )
                        Utils.showDismissibleSnackbar(errorMessage, view, resources)
                    })
            } else {
                Utils.showDismissibleSnackbar(resources.getText(R.string.invalid_repository_handle).toString(), view, resources)
            }
        }

        return view;
    }

    private fun validateInput(input: String): Boolean {
        val regex: Regex = Regex(repositoryHandleValidationRegex)
        return regex.matches(input)
    }

    private fun navigateToCommitListView(repository: GitRepository) {
        val action = RepositorySearchFragmentDirections.actionRepositorySearchFragmentToCommitListFragment(repository)
        findNavController().navigate(action)
    }
}