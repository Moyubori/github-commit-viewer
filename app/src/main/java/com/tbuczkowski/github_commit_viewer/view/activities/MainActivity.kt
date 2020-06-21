package com.tbuczkowski.github_commit_viewer.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tbuczkowski.github_commit_viewer.R
import com.tbuczkowski.github_commit_viewer.data_providers.GitRepositoryProvider

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        // init singleton instance with activity context
        GitRepositoryProvider.getInstance(this)
    }
}