package com.example.ecomadminbatch04.utils

import android.content.Context
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import java.text.SimpleDateFormat
import java.util.*

fun getFormattedDate(date: Long, pattern: String) =
    SimpleDateFormat(pattern).format(Date(date))

fun showInputDialog(context: Context, title: String, callback: (String) -> Unit) {
    val builder = AlertDialog.Builder(context).apply {
        setTitle(title)
        val editText: EditText = EditText(context)
        editText.hint = "Set new $title"
        setView(editText)
        setPositiveButton("SET"){dialog, v ->
            val value = editText.text.toString()
            // TODO: validate
            callback(value)
        }
        setNegativeButton("Close", null)
    }
    builder.create().show()
}