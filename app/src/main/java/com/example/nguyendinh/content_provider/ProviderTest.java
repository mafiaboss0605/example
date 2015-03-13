package com.example.nguyendinh.content_provider;

import android.app.Activity;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by NguyenDinh on 12/14/2014.
 */
public class ProviderTest extends Activity {
    EditText editISBN, editTitle;
    Button insert, retrieve;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_test);
        initView();
        getControl();
    }

    private void initView() {
        editISBN = (EditText) findViewById(R.id.editISBN);
        editTitle = (EditText) findViewById(R.id.editTitle);
        insert = (Button) findViewById(R.id.insertTitle);
        retrieve = (Button) findViewById(R.id.retrieveTitle);
        textView = (TextView) findViewById(R.id.retrieve);
    }

    private void getControl() {
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(BookProvider.ISBN, editISBN.getText().toString());
                values.put(BookProvider.TITLE, editTitle.getText().toString());
                Uri uri = getContentResolver().insert(BookProvider.CONTENT_URI, values);
                Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        retrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri allTitles = BookProvider.CONTENT_URI;
                Cursor c;
                CursorLoader cursorLoader = new CursorLoader(getBaseContext(), allTitles, null,
                        null, null, null);
                c = cursorLoader.loadInBackground();
                String s = "";
                if (c.moveToFirst()) {
                    do {
                        s += c.getString(c.getColumnIndex(BookProvider.ISBN)) + c
                                .getString(c.getColumnIndex(BookProvider.TITLE)) + "\n";
                    } while (c.moveToNext());
                }
                textView.setText(s);
            }
        });
    }
}
