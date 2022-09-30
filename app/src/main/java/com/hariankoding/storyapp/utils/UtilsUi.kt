package com.hariankoding.storyapp.utils

import android.app.Dialog
import android.content.Context
import com.hariankoding.storyapp.R

object UtilsUi {
    private lateinit var dialog: Dialog
    fun showDialog(context: Context) {
        dialog = Dialog(context)
        dialog.setContentView(R.layout.progress_dialog)
        dialog.setCancelable(false)
        dialog.show()
    }

    fun closeDialog() {
        dialog.dismiss()
    }
}