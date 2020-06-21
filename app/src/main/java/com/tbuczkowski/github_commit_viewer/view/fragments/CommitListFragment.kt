package com.tbuczkowski.github_commit_viewer.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListAdapter
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tbuczkowski.github_commit_viewer.R
import com.tbuczkowski.github_commit_viewer.Utils
import com.tbuczkowski.github_commit_viewer.model.Commit
import com.tbuczkowski.github_commit_viewer.model.GitRepository
import com.tbuczkowski.github_commit_viewer.view.adapters.CommitAdapter

class CommitListFragment : Fragment() {

    private val args: CommitListFragmentArgs by navArgs()

    private var selectedCommit: Commit? = null

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

        val recyclerView: RecyclerView = view.findViewById<RecyclerView>(R.id.commitsRecyclerView)
        val adapter: CommitAdapter = CommitAdapter(requireContext(), commits)
        recyclerView.adapter = adapter
        val tracker = SelectionTracker.Builder<Long>(
            "commitListViewSelection",
            recyclerView,
            StableIdKeyProvider(recyclerView),
            CommitAdapter.CommitItemDetailsLookup(recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()
        adapter.tracker = tracker

        // TODO: selection doesn't seem to be working fine
        // TODO: passing data from multiple selected items to share intent

//        recyclerView.adapter = adapter
//        recyclerView.setOnItemClickListener { parent, view, index, id ->
//            selectedCommit = adapter.getItem(index) as Commit
//            view.isSelected = true
//        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val sendButton: Button = view.findViewById<Button>(R.id.sendButton)
        sendButton.setOnClickListener {
            if (selectedCommit != null) {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, selectedCommit.toString())
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