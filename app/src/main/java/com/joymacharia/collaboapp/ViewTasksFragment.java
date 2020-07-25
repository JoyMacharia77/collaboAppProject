package com.joymacharia.collaboapp;

import android.content.SharedPreferences;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.joymacharia.collaboapp.models.User;
import com.joymacharia.collaboapp.models.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */

public class ViewTasksFragment extends Fragment {
    private RecyclerView taskRecyclerView;
    private ArrayList<Task> list;
    private TaskAdapter taskAdapter;
    private SharedPreferences preferences;

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
        list = new ArrayList<>();
        taskAdapter = new TaskAdapter(list,getContext());
        taskRecyclerView.setAdapter(taskAdapter);
//        initializeData();
        getTasks();
        //Implement Swiping and moving of Card Items
        ItemTouchHelper helper= new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT|ItemTouchHelper.DOWN|ItemTouchHelper.UP, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to= viewHolder.getAdapterPosition();
                Collections.swap(list,from,to);
                taskAdapter.notifyItemMoved(from,to);

                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                list.remove(viewHolder.getAdapterPosition());
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
        list.clear();
        // Create an array list of dessert Recipes with the title,description and images
        for (int i=0; i<taskTitles.length; i++)
        {
            list.add(new Task(taskImages.getResourceId(i,0), taskTitles[i], taskDeadlines[i]));
        }
        // Clean Up the data in the typed array
        taskImages.recycle();
        // Notify the adapter of the change in the data set
        taskAdapter.notifyDataSetChanged();
    }
    private void getTasks(){
        list = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST, constants.VIEW_TASK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject object = new JSONObject(response);
                    if(object.getBoolean("success")){
                        JSONArray tasks = new JSONArray(object.getString("tasks"));
                        for(int i = 0; i< tasks.length(); i++){
                            JSONObject task =  tasks.getJSONObject(i);
                            JSONObject user = task.getJSONObject("user");

                            User mUser = new User();
                            mUser.setId(user.getInt("id"));

                            Task mTask = new Task();
                            mTask.setId(task.getInt("id"));
                            mTask.setUser(mUser);
                            mTask.setDate(task.getString("created_at"));
                            mTask.setTask(task.getString("task"));
                            list.add(mTask);
                        }
                    }
                    taskAdapter = new TaskAdapter(list,getContext());
                    taskRecyclerView.setAdapter(taskAdapter);

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){

            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
//                map.put("date", txtEmail.getText().toString().trim());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
}

