package com.joymacharia.collaboapp;

import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewTasksFragment extends Fragment {
    private RecyclerView taskRecyclerView;
    private ArrayList<Task> taskData;
    private TaskAdapter taskAdapter;

    public ViewTasksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_view_tasks, container, false);
        taskRecyclerView = rootView.findViewById(R.id.recycler_tasks);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskData = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskData,getContext());
        taskRecyclerView.setAdapter(taskAdapter);
        initializeData();
        //Implement Swiping and moving of Card Items
        ItemTouchHelper helper= new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT|ItemTouchHelper.DOWN|ItemTouchHelper.UP, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to= viewHolder.getAdapterPosition();
                Collections.swap(taskData,from,to);
                taskAdapter.notifyItemMoved(from,to);

                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                taskData.remove(viewHolder.getAdapterPosition());
                taskAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });
        helper.attachToRecyclerView(taskRecyclerView);

        return rootView;
    }

    private void initializeData()
    {
        String[] taskTitles = getResources().getStringArray(R.array.task_titles);
        String[] taskDeadlines = getResources().getStringArray(R.array.task_deadlines);
        TypedArray taskImages= getResources().obtainTypedArray(R.array.task_images);

        //Clear Existing data to avoid duplications
        taskData.clear();
        // Create an array list of dessert Recipes with the title,description and images
        for (int i=0; i<taskTitles.length; i++)
        {
            taskData.add(new Task(taskImages.getResourceId(i,0), taskTitles[i], taskDeadlines[i]));
        }
        // Clean Up the data in the typed array
        taskImages.recycle();
        // Notify the adapter of the change in the data set
        taskAdapter.notifyDataSetChanged();
    }
}
