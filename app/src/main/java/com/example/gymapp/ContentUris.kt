package com.example.gymapp

import android.net.Uri

class ProviderContract {
    companion object {
        const val AUTHORITY = "com.example.gymapp.AppContentProvider"
        const val BASE_URI = "content://$AUTHORITY"
        val FETCH_TAGS_URI = Uri.parse("$BASE_URI/tags")
        val INSERT_TAGS_URI = Uri.parse("$BASE_URI/tags/#")

    }
}

