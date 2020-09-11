package com.joymacharia.collaboapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NEWAddTaskActivity extends AppCompatActivity {

    private static final String TAG = "NEWAddTaskActivity";

    DatabaseHelper mDatabaseHelper;
    private Button btnAdd, btnViewTasks;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_n_e_w_add_task);
        editText = (EditText) findViewById(R.id.add_new_task);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnViewTasks = (Button) findViewById(R.id.btnView);
        mDatabaseHelper = new DatabaseHelper(this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEntry = editText.getText().toString();
                if (editText.length() != 0)
                {
                     AddTasks(newEntry);
                     editText.setText("");
                }else {
                    toastMessage("You Cannot Add An Empty Task!");
                }
            }
        });

        btnViewTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(NEWAddTaskActivity.this, ListTasksActivity.class);
                startActivity(intent);
            }
        });

    }

    public void AddTasks(String newEntry)
    {
        boolean insertTasks = mDatabaseHelper.addTasks(newEntry);

        if (insertTasks)
        {
            toastMessage("New Task Successfully Added!");
        }
        else
        {
            toastMessage("Something Went Wrong");
        }
    }

    private void toastMessage(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
