package com.tbuczkowski.github_commit_viewer.data_providers

import com.tbuczkowski.github_commit_viewer.model.Commit
import com.tbuczkowski.github_commit_viewer.model.GitRepository
import com.tbuczkowski.github_commit_viewer.model.dto.GitHubCommit
import com.tbuczkowski.github_commit_viewer.model.dto.GitHubRepository
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

class GitRepositoryProvider {

    private val gitHubService: GitHubService = GitHubService.create()

    // TODO: caching

    fun fetchGitRepository(handle: String) : Observable<GitRepository> {
        val splitHandle: List<String> = handle.split("/")
        val owner: String = splitHandle[0]
        val name: String = splitHandle[1]
        val gitHubRepositoryCall: Observable<GitHubRepository> = gitHubService.fetchRepositoryInfo(owner, name)
        val gitHubCommitsCall: Observable<List<GitHubCommit>> = gitHubService.fetchCommits(owner, name);
        return Observable.zip(gitHubRepositoryCall, gitHubCommitsCall, BiFunction<GitHubRepository, List<GitHubCommit>, GitRepository>{ repository, commits ->
            val mappedCommits: List<Commit> = commits.map {
                Commit(it.commitInternal?.message ?: "NULL MSG", it.sha ?: "NULL SHA", it.commitInternal?.author?.name ?: "NULL AUTHOR")
            }
            GitRepository(handle, repository.id?.toString() ?: "NULL REPOSITORY ID", mappedCommits)
        })
    }

}