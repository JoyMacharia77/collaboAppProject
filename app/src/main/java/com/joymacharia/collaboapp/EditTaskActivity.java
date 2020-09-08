package com.joymacharia.collaboapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditTaskActivity extends AppCompatActivity {

    private static final String TAG = "EditTaskActivity";

    private Button btnSave,btnDelete;
    private EditText editable_item;

    DatabaseHelper mDatabaseHelper;

    private String selectedTask;
    private int selectedID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        editable_item = (EditText) findViewById(R.id.editable_item);
        mDatabaseHelper = new DatabaseHelper(this);

        //get the intent extra from the ListTaskActivity
        Intent receivedIntent = getIntent();

        //now get the itemID we passed as an extra
        //NB: -1 is just the default value
        selectedID = receivedIntent.getIntExtra("id",-1);

        //now get the name we passed as an extra
        selectedTask = receivedIntent.getStringExtra("task");

        //set the text to show the current selected name
        editable_item.setText(selectedTask);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item = editable_item.getText().toString();
                if(!item.equals("")){
                    mDatabaseHelper.updateTask(item,selectedID, selectedTask);

                    Intent editTask = new Intent(EditTaskActivity.this, ListTasksActivity.class);
                    startActivity(editTask);
                }else{
                    toastMessage("You Must Enter a Task");
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseHelper.deleteTask(selectedID, selectedTask);
                editable_item.setText("");
                toastMessage("Task Deleted Successfully");

                Intent deleteTask = new Intent(EditTaskActivity.this, ListTasksActivity.class);
                startActivity(deleteTask);
            }
        });

    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
