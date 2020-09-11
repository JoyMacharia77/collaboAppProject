package com.joymacharia.collaboapp.Fragments;


import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.joymacharia.collaboapp.Adapters.taskAdapter;
import com.joymacharia.collaboapp.R;
import com.joymacharia.collaboapp.TaskAdapter;
import com.joymacharia.collaboapp.constants;
import com.joymacharia.collaboapp.models.User;
import com.joymacharia.collaboapp.models.Task;
import com.joymacharia.collaboapp.models.newTask;
import com.joymacharia.collaboapp.models.newUser;

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

public class NewViewTasksFragment extends Fragment {
//    public static BluetoothManager taskRecyclerView;
    //    public static MenuBuilder arrayList;
    private View rootView;
    public static RecyclerView taskRecyclerView = null ;
    public static ArrayList<newTask> arrayList = new ArrayList<newTask>();
    private com.joymacharia.collaboapp.Adapters.taskAdapter taskAdapter;
    private SharedPreferences preferences;

    public NewViewTasksFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_view_tasks, container, false);

        init();
        return rootView;
    }

    private void init(){
        preferences = getContext().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        taskRecyclerView = rootView.findViewById(R.id.recycler_tasks);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
       taskRecyclerView.setHasFixedSize(true);

        taskAdapter = new taskAdapter( getContext(),arrayList);
        taskRecyclerView.setAdapter(taskAdapter);

//        initializeData();
        getTasks();
        //Implement Swiping and moving of Card Items
        ItemTouchHelper helper= new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT|ItemTouchHelper.DOWN|ItemTouchHelper.UP, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to= viewHolder.getAdapterPosition();
                Collections.swap(arrayList,from,to);
                taskAdapter.notifyItemMoved(from,to);

                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                arrayList.remove(viewHolder.getAdapterPosition());
                taskAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });
        helper.attachToRecyclerView(taskRecyclerView);
    }



//        private void initializeData()
//    {
//        String[] taskTitles = getResources().getStringArray(R.array.task_titles);
//        String[] taskDeadlines = getResources().getStringArray(R.array.task_deadlines);
//        TypedArray taskImages= getResources().obtainTypedArray(R.array.task_images);
//
//        //Clear Existing data to avoid duplications
//        arrayList.clear();
//        // Create an array list of dessert Recipes with the title,description and images
//        for (int i=0; i<taskTitles.length; i++)
//        {
//            arrayList.add(new Task(taskImages.getResourceId(i,0), taskTitles[i], taskDeadlines[i]));
//        }
//        // Clean Up the data in the typed array
//        taskImages.recycle();
//        // Notify the adapter of the change in the data set
//        taskAdapter.notifyDataSetChanged();
//    }
    private void getTasks(){
        arrayList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, constants.VIEW_TASK, response -> {
            try{
                JSONObject object = new JSONObject(response);
                if(object.getBoolean("success")) {
                    JSONArray tasks = new JSONArray(object.getString("tasks"));
                    for (int i = 0; i < tasks.length(); i++) {
                        JSONObject taskObject = tasks.getJSONObject(i);
                        JSONObject userObject = taskObject.getJSONObject("user");

                        newUser mUser = new newUser();
                        mUser.setId(userObject.getInt("id"));
                        mUser.setUserName(userObject.getString("name") + "" + userObject.getString("lastname"));
                        mUser.setPhoto(userObject.getString("photo"));

                        newTask mTask = new newTask();
                        mTask.setId(taskObject.getInt("id"));
                        mTask.setUser(mUser);
                        mTask.setTaskTitle(taskObject.getString("task"));
                        mTask.setTaskDeadline(taskObject.getString("deadline"));
                        mTask.setDate(taskObject.getString("created_at"));
                        mTask.setTaskDeadline(taskObject.getString("deadline"));
                        arrayList = new ArrayList<>();
                        arrayList.add(mTask);

                    }
                    taskAdapter = new taskAdapter(getContext(), arrayList);
                    taskRecyclerView.setAdapter(taskAdapter);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }, Throwable::printStackTrace){
            //provide token in header

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
               String token = preferences.getString("token","");
                HashMap<String,String> map = new HashMap<>();
                 map.put("Authorization","Bearer "+token);
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
}


