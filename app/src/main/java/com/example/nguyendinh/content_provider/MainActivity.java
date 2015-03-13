package com.example.nguyendinh.content_provider;

import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    static TextView tv1, tv2;
    ListView lv;
    Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        lv = (ListView) findViewById(R.id.listView);
        Uri allContacts = Uri.parse("content://contacts/people");
        CursorLoader cursorLoader = new CursorLoader(this, allContacts, null, null, null, null);
        c = cursorLoader.loadInBackground();
        String[] columns = {ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME};
        int[] views = {R.id.tv1, R.id.tv2};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.activity_main, c,
                columns, views, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        lv.setAdapter(adapter);
        printContact();
    }

    public void printContact() {
        if (c.moveToFirst()) {
            do {
                String contactID = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                String contact = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Log.w("PrintContact", contact);
                String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts
                        .HAS_PHONE_NUMBER));
//                if (hasPhone == 1) {
//                    Cursor phoneCursor = getContentResolver().query(ContactsContract
//                                    .CommonDataKinds.Phone.CONTENT_URI, null,
//                            ContactsContract.CommonDataKinds.Phone._ID + "=" + contactID, null, null);
//                    while (phoneCursor.moveToNext()) {
//                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex
//                                (ContactsContract.CommonDataKinds.Phone.NUMBER));
//                        Log.w("Phone Number", phoneNumber);
//                    }
//                    phoneCursor.close();
//                }
//
            } while (c.moveToNext());
//
        }
        c.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify an parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
