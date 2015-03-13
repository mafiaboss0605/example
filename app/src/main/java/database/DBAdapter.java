package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by NguyenDinh on 12/13/2014.
 */
public class DBAdapter {
    static final String ID = "id";
    static final String NAME = "name";
    static final String EMAIL = "email";
    static final String TAG = "TAG";
    static final String DBName = "MyDB";
    static final String TABLE = "contact";
    static final int VERSION = 2;
    static final String DB_CREATE = "create table contact(id integer primary key autoincrement, " +
            "name text not null, email text not null);";
    final Context context;
    SQLiteDatabase db;
    DBHelper dbHelper;

    public DBAdapter(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertContact(String name, String email) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(EMAIL, email);
        return db.insert(TABLE, null, contentValues);

    }

    public long deleteContact(long rowID) {
        return db.delete(TABLE, ID + "=" + rowID, null);
    }

    public Cursor getAllContacts() {
        return db.query(TABLE, null, null, null, null, null, null);
    }

    public Cursor getContact(long rowID) {
        Cursor c = db.query(TABLE, null, ID + "=" + rowID, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public int updateContact(long rowID, String name, String email) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(EMAIL, email);
        return db.update(TABLE, contentValues, ID + "=" + rowID, null);
    }

    private class DBHelper extends SQLiteOpenHelper {


        public DBHelper(Context context) {
            super(context, DBName, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DB_CREATE);
            } catch (SQLiteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("Upgrade", "Upgrade from " + oldVersion + "to" + newVersion);
            db.execSQL("drop table if exists " + TABLE);
            onCreate(db);
        }
    }
}
