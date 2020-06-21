package com.tbuczkowski.github_commit_viewer.data_providers

import com.tbuczkowski.github_commit_viewer.model.GitRepository

class GitRepositoryCache {

    private val cache: HashMap<String, GitRepository> = hashMapOf()

    fun insert(repository: GitRepository) {
        cache[repository.handle] = repository
    }

    fun contains(key: String): Boolean {
        return cache.containsKey(key)
    }

    fun get(key: String): GitRepository {
        return cache[key]!!
    }

    fun getAll(): List<GitRepository> {
        return cache.values.toList()
    }

}