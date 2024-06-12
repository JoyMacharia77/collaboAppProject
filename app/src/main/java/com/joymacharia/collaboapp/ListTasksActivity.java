package com.joymacharia.collaboapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListTasksActivity extends AppCompatActivity {

    private static final String TAG = "ListTasksActivity";
    DatabaseHelper mDatabaseHelper;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tasks);
        mListView = (ListView) findViewById(R.id.listView);
        mDatabaseHelper = new DatabaseHelper(this);

        populateListView();
    }

    private void populateListView()
    {
        Log.d(TAG, "populateListView: Displaying data in the ListView.");

        //get the data and append to a list
        Cursor tasks = mDatabaseHelper.getTasks();
        ArrayList<String> listTasks = new ArrayList<>();
        while (tasks.moveToNext())
        {
            //get the value from database in column 1
            // then add to array list
            listTasks.add(tasks.getString(1));
        }
        //create list Adapter and set the Adapter
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listTasks);
        mListView.setAdapter(adapter);

        //onItemClickListener to the ListView
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              String task = parent.getItemAtPosition(position).toString();
              Log.d(TAG, "onItemClick: You Clicked on " + task);

              //Get ID Associated with that name
              Cursor tasks = mDatabaseHelper.getItemID(task);
              int taskID = -1;
              Log.d(TAG, "reached here " + taskID);
              while (tasks.moveToNext())
              {
                  taskID = tasks.getInt(0);
                  Log.d(TAG, "WHILE LOOP WORKING " + taskID);
              }
              if (taskID > -1)
              {
                  Log.d(TAG, "onItemClick: The ID is: " + taskID);

                  Intent editScreenIntent = new Intent(ListTasksActivity.this, EditTaskActivity.class);
                  editScreenIntent.putExtra("id", taskID);
                  editScreenIntent.putExtra("task", task);
                  startActivity(editScreenIntent);

              } else
              {
                  toastMessage("No ID Associated with that Task");
              }
              tasks.close();

            }
        });
    }

    private void toastMessage(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
