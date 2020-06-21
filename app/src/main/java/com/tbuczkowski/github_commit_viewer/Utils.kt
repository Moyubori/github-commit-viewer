package com.tbuczkowski.github_commit_viewer

import android.content.res.Resources
import android.view.View
import com.google.android.material.snackbar.Snackbar

object Utils {

    fun showDismissibleSnackbar(message: String, view: View, resources: Resources) {
        val snackbar: Snackbar =
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        snackbar.setAction(resources.getText(R.string.dismiss)) {
            snackbar.dismiss()
        }
        snackbar.show()
    }

}