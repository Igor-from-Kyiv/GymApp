package com.example.gymapp

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri

class AppContentProvider: ContentProvider() {
    val TAGS_URI_MATCH = 1
    val INSERT_TAGS_URI_MATCH = 2
    val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    lateinit var db: AppDataBase

    override fun onCreate(): Boolean {
        db = AppDataBase(context!!)
        uriMatcher.addURI(ProviderContract.AUTHORITY, "tags", TAGS_URI_MATCH)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            TAGS_URI_MATCH -> {
                db.fetchTags()}
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
//        when (uriMatcher.match(uri)) {
//            1 -> "vnd.android.cursor.dir/vnd.com.example.android.gymapp.contentprovider.tags"
//        }
        return "vnd.android.cursor.dir/vnd.com.example.android.gymapp.AppContentProvider.tags"
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {

        val id = db.insertTag(values?.get("name").toString())
        getContext()?.getContentResolver()?.notifyChange(uri, null)
        return ProviderContract.FETCH_TAGS_URI
//        return Uri.parse(ProviderContract.FETCH_TAGS_URI.toString() + "/" + id.toString())
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }
}