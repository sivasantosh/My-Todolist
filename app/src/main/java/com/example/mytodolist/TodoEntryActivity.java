package com.example.mytodolist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;

public class TodoEntryActivity extends AppCompatActivity {
    String entry_text = null;
    boolean mark_imp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_entry);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_todo_entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addNewEntry:
                EditText et = (EditText) findViewById(R.id.editText);
                CheckBox cb = (CheckBox) findViewById(R.id.checkBox);

                entry_text = et.getText().toString();
                mark_imp = cb.isChecked();

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
        data.putExtra("mark_imp", mark_imp);

        setResult(RESULT_OK, data);

        super.finish();
    }
}
