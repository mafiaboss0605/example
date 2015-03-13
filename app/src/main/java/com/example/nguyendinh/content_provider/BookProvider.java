package com.example.nguyendinh.content_provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by NguyenDinh on 12/13/2014.
 */
public class BookProvider extends ContentProvider {
    final static String PROVIDER_NAME = "BookProvider";
    final static Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/books");

    final static String ID = "id";
    final static String TITLE = "title";
    final static String ISBN = "isbn";

    final static int BOOKS = 1;
    final static int BOOK_ID = 2;
    static final String DATABASE_NAME = "MyBooks";
    static final String TABLE = "titles";
    static final String CREATE_TABLE = "create table " + TABLE + "(id integer primary key " +
            "autoincrement, title text not null, isbn text not null);";
    static final int VERSION = 1;
    private final static UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "books", BOOKS);
        uriMatcher.addURI(PROVIDER_NAME, "book/#", BOOK_ID);
    }

    private SQLiteDatabase db;
    private DBHelper dbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        return (db == null) ? false : true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(TABLE);
        if (uriMatcher.match(uri) == BOOK_ID) {
            builder.appendWhere(ID + "=" + uri.getPathSegments().get(1));
        }
        if (sortOrder == null || sortOrder.equals("")) {
            sortOrder = "title";
        }
        Cursor c;
        c = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case BOOK_ID:
                return "vnd.android.cursor.item/vnd.books";
            case BOOKS:
                return "vnd.android.cursor.dir/vnd.books";
            default:
                throw new IllegalArgumentException("Unsupported content uri");
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.insert(TABLE, null, values);
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case BOOKS:
                count = db.delete(TABLE, selection, selectionArgs);
                break;
            case BOOK_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(TABLE, ID + "=" + id + (!TextUtils.isEmpty(selection) ? "AND " +
                                "(" + selection + ")" : ""),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported content uri");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case BOOKS:
                count = db.update(TABLE, values, selection, selectionArgs);
                break;
            case BOOK_ID:
                String id = uri.getPathSegments().get(1);
                count = db.update(TABLE, values, ID + "=" + id + (!TextUtils.isEmpty(selection) ?
                        "AND" + "(" + selection + ")" : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported content uri");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("Upgrade", "Databse upgrade from " + oldVersion + "to " + newVersion);
            db.execSQL("drop table if exists " + TABLE);
            onCreate(db);
        }
    }
}
