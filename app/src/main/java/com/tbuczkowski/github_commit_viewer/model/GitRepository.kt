package com.tbuczkowski.github_commit_viewer.model

data class GitRepository (val handle: String, val commits: List<Commit> = emptyList())