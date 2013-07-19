/**
 * 
 */
package com.example.collection.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class CollectionProvider extends ContentProvider {
    private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int WORDS = 1;
    private static final String AUTHORITY = "com.example.providers.collection";
    public final static Uri DOLLECTION_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/collection");
    private final static String NULL_CALLBACK = "_id";
    private DBService mDbService;
    static {
        matcher.addURI(AUTHORITY, "collection", WORDS);
    }

    @Override
    public boolean onCreate() {
        mDbService = DBService.getInstance(getContext());
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long rowId = mDbService.insertValue(values);
        if (rowId > 0) {
            Uri wordUri = ContentUris.withAppendedId(uri, rowId);
            getContext().getContentResolver().notifyChange(wordUri, null);
            return wordUri;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int num = 0;
        switch (matcher.match(uri)) {
            case WORDS:
                num = mDbService.deleteValue(selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("δ֪Uri:" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return num;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        int num = 0;
        switch (matcher.match(uri)) {
            case WORDS:
                num = mDbService.updateValue(values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("δ֪Uri:" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return num;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        switch (matcher.match(uri)) {
            case WORDS:
                return mDbService.queryVaule(projection, selection,
                        selectionArgs, sortOrder);
            default:
                throw new IllegalArgumentException("δ֪Uri:" + uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        switch (matcher.match(uri)) {
            case WORDS:
                return "vnd.android.cursor.dir/com.example.collection";
            default:
                throw new IllegalArgumentException("δ֪Uri:" + uri);
        }
    }

    @Override
    public void shutdown() {
        super.shutdown();
        mDbService.close();
    }
}