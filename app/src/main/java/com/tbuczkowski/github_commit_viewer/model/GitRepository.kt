package com.tbuczkowski.github_commit_viewer.model

import java.io.Serializable

data class GitRepository (val handle: String, val id: String, val commits: List<Commit> = emptyList()) : Serializable {

    fun copyWith(handle: String? = null, id: String? = null, commits: List<Commit>? = null): GitRepository {
        return GitRepository(handle ?: this.handle, id ?: this.id, commits ?: this.commits)
    }

}