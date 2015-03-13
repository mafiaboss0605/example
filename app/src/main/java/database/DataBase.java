package database;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nguyendinh.content_provider.R;

/**
 * Created by NguyenDinh on 12/13/2014.
 */
public class DataBase extends Activity {
    Button btnSave, btnLoad;
    EditText editName, editEmail;
    TextView editLoad;
    DBAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database);
        initView();
        getControl();
    }

    private void initView() {
        dbAdapter = new DBAdapter(this);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnLoad = (Button) findViewById(R.id.btnLoad);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editName = (EditText) findViewById(R.id.editName);
        editLoad = (TextView) findViewById(R.id.editLoad);
    }

    private void getControl() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbAdapter.open();
                if (dbAdapter.insertContact(editName.getText().toString(),
                        editEmail.getText().toString()) != -1) {
                    Toast.makeText(getBaseContext(), "Saved", Toast.LENGTH_SHORT);
                }
                dbAdapter.close();

            }
        });
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbAdapter.open();
                Cursor c = dbAdapter.getContact(1);
                editLoad.setText(c.getString(c.getColumnIndex(dbAdapter.NAME)) + c.getString(c
                        .getColumnIndex(dbAdapter.EMAIL)));
                dbAdapter.close();
            }
        });
    }
}
