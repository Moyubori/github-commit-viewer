package com.tbuczkowski.github_commit_viewer.model.dto

import com.google.gson.annotations.SerializedName

class GitHubCommit {
    @SerializedName("sha")
    var sha: String? = null
    @SerializedName("commit")
    var commitInternal: GitHubCommitInternal? = null
}