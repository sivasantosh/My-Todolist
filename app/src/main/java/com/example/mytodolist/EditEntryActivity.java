package com.example.mytodolist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class EditEntryActivity extends AppCompatActivity {
    String entry_text;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_entry);

        EditText editText = (EditText) findViewById(R.id.editText2);

        Intent data = getIntent();
        entry_text = data.getStringExtra("entry_text");
        pos = data.getIntExtra("entry_pos", -1);

        editText.setText(entry_text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveEditEntry:
                EditText e = (EditText) findViewById(R.id.editText2);
                entry_text = e.getText().toString();
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("entry_text", entry_text);
        data.putExtra("entry_pos", pos);

        setResult(RESULT_OK, data);

        super.finish();
    }
}
