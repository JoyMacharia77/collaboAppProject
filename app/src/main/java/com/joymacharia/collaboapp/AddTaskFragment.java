package com.joymacharia.collaboapp;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.joymacharia.collaboapp.models.User;
import com.joymacharia.collaboapp.models.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */

public class AddTaskFragment extends Fragment {
    //initialize
    private RecyclerView taskRecyclerView;
    private TaskAdapter taskAdapter;
    private EditText deadline,task;
    private Button addbtn;
    private View view;
    private ProgressDialog dialog;
    private ArrayList<Task> list;


    public AddTaskFragment() {
        // Required empty public constructor
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_task,container,false);
        init();
        return view;
    }

    private void init() {
        deadline = view.findViewById(R.id.deadline);
        task = view.findViewById(R.id.task);
        addbtn = view.findViewById(R.id.add_btn);
        dialog = new ProgressDialog(getContext());
        taskAdapter = new TaskAdapter(list,getContext());


        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask();
            }
        });


    }

    public void addTask(){
        final String  addTask = task.getText().toString();
        final String deadlin = deadline.getText().toString();
        dialog.setMessage("adding new task");
        dialog.show();

        if(addTask.length()>0){
            list = new ArrayList<>();
            StringRequest request = new StringRequest(Request.Method.POST, constants.ADD_TASK, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject object = new JSONObject(response);
                        if(object.getBoolean("success")){
                            JSONObject task = object.getJSONObject("task");
                            JSONObject user = task.getJSONObject("user");

                            Task t = new Task();
                            User u = new User();
                            u.setId(user.getInt("id"));
                            t.setUser(u);
                            t.setId(task.getInt("id"));
                            t.setDate(task.getString("created_at"));


                            list.add(t);
//                            taskRecyclerView.getAdapter().notifyDataSetChanged();
//                        addTask.setText("");

                        }
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    error.printStackTrace();
                    dialog.dismiss();
                }
            }){
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("task",addTask);
                    map.put("deadline",deadlin);
                    return map;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(request);
        }

    }
}
