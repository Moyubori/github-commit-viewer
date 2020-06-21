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
import com.tbuczkowski.github_commit_viewer.data_providers.GitRepositoryProvider
import com.tbuczkowski.github_commit_viewer.model.Commit
import com.tbuczkowski.github_commit_viewer.model.GitRepository
import com.tbuczkowski.github_commit_viewer.view.adapters.GitRepositoryAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

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
        listView.setOnItemClickListener { parent, view, index, id ->
            val repository: GitRepository = adapter.getItem(index) as GitRepository
            navigateToCommitListView(repository)
        }

        val repoSearchButton: Button = view.findViewById(R.id.repoSearchButton)
        repoSearchButton.setOnClickListener {
            val nameInputField: EditText = view.findViewById<EditText>(R.id.repoNameInput)
            val repoHandle: String = nameInputField.text.toString()
            // TODO: input validation
            gitRepositoryProvider.fetchRepository(repoHandle)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                navigateToCommitListView(it)
            },  {
                    val exception: HttpException = it as HttpException
                    val errorMessage: String = String.format(resources.getText(R.string.error_fetching_repo).toString(), exception.code())
                    val snackbar: Snackbar = Snackbar.make(view, errorMessage, Snackbar.LENGTH_SHORT)
                    snackbar.setAction(resources.getText(R.string.dismiss)) {
                        snackbar.dismiss()
                    }
                    snackbar.show()
                })
        }

        return view;
    }

    private fun navigateToCommitListView(repository: GitRepository) {
        val action = RepositorySearchFragmentDirections.actionRepositorySearchFragmentToCommitListFragment(repository)
        findNavController().navigate(action)
    }
}