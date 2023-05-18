package com.example.gymapp

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView

class AppCursorAdapter(ctx: Context, cursor: Cursor, flags: Int): CursorAdapter(ctx, cursor, flags) {
    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
        return LayoutInflater.from(context).inflate(com.google.android.material.R.layout.support_simple_spinner_dropdown_item, parent, false)
    }

    override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
        val optionText = cursor?.getString(cursor.getColumnIndexOrThrow("name"))
        view?.findViewById<TextView>(android.R.id.text1)?.text = optionText
        //...
    }

    override fun swapCursor(newCursor: Cursor?): Cursor {
        // Update cursor with new data and notify adapter to update the Spinner
        val c = super.swapCursor(newCursor)
        notifyDataSetChanged()
        return c
    }
}