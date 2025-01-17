package com.tbuczkowski.github_commit_viewer.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.tbuczkowski.github_commit_viewer.R
import com.tbuczkowski.github_commit_viewer.Utils
import com.tbuczkowski.github_commit_viewer.model.Commit
import com.tbuczkowski.github_commit_viewer.model.GitRepository
import com.tbuczkowski.github_commit_viewer.view.adapters.CommitAdapter

class CommitListFragment : Fragment() {

    private val args: CommitListFragmentArgs by navArgs()

//    private var selectedCommit: Commit? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.commit_list_fragment, container, false)

        val repository: GitRepository = args.repository
        val commits: List<Commit> = repository.commits

        val repositoryIdTextView: TextView = view.findViewById<TextView>(R.id.repositoryIdText)
        val formattedRepositoryIdText: String = String.format(resources.getString(R.string.commit_list_repository_id), repository.id)
        repositoryIdTextView.text = formattedRepositoryIdText

        val listView: ListView = view.findViewById<ListView>(R.id.commitsListView)
        val adapter: CommitAdapter = CommitAdapter(requireContext(), commits)
        listView.adapter = adapter

        val sendButton: Button = view.findViewById<Button>(R.id.sendButton)
        sendButton.setOnClickListener {
            val selectedCommits: List<Commit> = adapter.getAndClearCheckedCommits()
            if (selectedCommits.isNotEmpty()) {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, selectedCommits.joinToString {
                        it.toString()
                    })
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            } else {
                Utils.showDismissibleSnackbar(resources.getText(R.string.no_commits_selected_message).toString(), view, resources)
            }
        }

        return view
    }

}