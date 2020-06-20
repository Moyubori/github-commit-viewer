package com.tbuczkowski.github_commit_viewer.model

import java.io.Serializable

data class Commit (val message: String, val sha: String, val author: String) : Serializable {

    override fun toString(): String {
        return "Commit(message='$message', sha='$sha', author='$author')"
    }

}