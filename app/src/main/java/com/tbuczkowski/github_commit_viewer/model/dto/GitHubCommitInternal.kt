package com.tbuczkowski.github_commit_viewer.model.dto

import com.google.gson.annotations.SerializedName

class GitHubCommitInternal {
    @SerializedName("message")
    var message: String? = null
    @SerializedName("author")
    var author: GitHubAuthor? = null

}