package com.tbuczkowski.github_commit_viewer.model.dto

import com.google.gson.annotations.SerializedName

class GitHubAuthor {
    @SerializedName("name")
    var name: String? = null
}