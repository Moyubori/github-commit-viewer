package com.tbuczkowski.github_commit_viewer.data_providers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.tbuczkowski.github_commit_viewer.SingletonHolder
import com.tbuczkowski.github_commit_viewer.model.Commit
import com.tbuczkowski.github_commit_viewer.model.GitRepository
import com.tbuczkowski.github_commit_viewer.model.dto.GitHubCommit
import com.tbuczkowski.github_commit_viewer.model.dto.GitHubRepository
import com.tbuczkowski.github_commit_viewer.model.exceptions.UnableToFetchRepositoryException
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

class GitRepositoryProvider private constructor(context: Context) {

    private val gitHubService: GitHubService = GitHubService.create()
    private val cache: GitRepositoryCache = GitRepositoryCache()
    private val connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    companion object : SingletonHolder<GitRepositoryProvider, Context>(::GitRepositoryProvider)

    fun fetchRepository(handle: String) : Observable<GitRepository> {
        val handleExistsInCache: Boolean = cache.contains(handle)
        val hasConnection: Boolean = hasInternetConnection()
        if (hasConnection) {
            val splitHandle: List<String> = handle.split("/")
            val owner: String = splitHandle[0]
            val name: String = splitHandle[1]
            if (handleExistsInCache) {
                return fetchCommitsFromAPIAndUpdateCache(handle, owner, name)
            } else {
                return fetchRepositoryInfoAndCommitsFromAPI(handle, owner, name)
            }
        } else if (handleExistsInCache) {
            return Observable.create { emitter ->
                val cachedRepository: GitRepository = cache.get(handle)
                emitter.onNext(cachedRepository)
            }
        }
        throw UnableToFetchRepositoryException("Could not download data from API and could not find a cached instance.")
    }

    fun getCachedRepositories(): List<GitRepository> {
        return cache.getAll()
    }

    private fun fetchCommitsFromAPIAndUpdateCache(handle: String, owner: String, name: String): Observable<GitRepository> {
        val gitHubCommitsCall: Observable<List<GitHubCommit>> =
            gitHubService.fetchCommits(owner, name);
        return Observable.create { emitter ->
            gitHubCommitsCall.observeOn(Schedulers.computation())
                .subscribeOn(Schedulers.computation()).subscribe({
                val mappedCommits: List<Commit> = mapDtoCommitsToModel(it)
                val cachedRepository: GitRepository = cache.get(handle)
                val updatedRepository: GitRepository =
                    cachedRepository.copyWith(commits = mappedCommits)
                cache.insert(updatedRepository)
                emitter.onNext(updatedRepository)
            }, {
                emitter.onError(it)
            })
        }
    }

    private fun fetchRepositoryInfoAndCommitsFromAPI(handle: String, owner: String, name: String): Observable<GitRepository> {
        val gitHubRepositoryCall: Observable<GitHubRepository> =
            gitHubService.fetchRepositoryInfo(owner, name)
        val gitHubCommitsCall: Observable<List<GitHubCommit>> =
            gitHubService.fetchCommits(owner, name);
        return Observable.zip(
            gitHubRepositoryCall,
            gitHubCommitsCall,
            BiFunction<GitHubRepository, List<GitHubCommit>, GitRepository> { repository, commits ->
                val mappedCommits: List<Commit> = mapDtoCommitsToModel(commits)
                val fetchedRepository: GitRepository = GitRepository(
                    handle,
                    repository.id?.toString() ?: "NULL REPOSITORY ID",
                    mappedCommits
                )
                cache.insert(fetchedRepository)
                fetchedRepository
            })
    }

    private fun mapDtoCommitsToModel(dtoCommits: List<GitHubCommit>) : List<Commit> {
        val mappedCommits: List<Commit> = dtoCommits.map {
            Commit(
                it.commitInternal?.message ?: "NULL MSG",
                it.sha ?: "NULL SHA",
                it.commitInternal?.author?.name ?: "NULL AUTHOR"
            )
        }
        return mappedCommits
    }

    private fun hasInternetConnection (): Boolean {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
        return false;
    }

}