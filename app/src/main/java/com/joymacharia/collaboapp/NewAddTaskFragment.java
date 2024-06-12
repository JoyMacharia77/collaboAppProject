package com.joymacharia.collaboapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewAddTaskFragment extends Fragment {

    private Button btnAdd;
    private  Button btnView;
    private View view;

    public NewAddTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_add_task, container, false);
        init();
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_new_add_task, container, false);
        return view;
    }

    private void init()
    {
        btnAdd = view.findViewById(R.id.btnAdd);
        btnView = view.findViewById(R.id.btnView);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(getContext(), NEWAddTaskActivity.class);
                startActivity(add);
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view = new Intent(getContext(),ListTasksActivity.class);
                startActivity(view);
            }
        });
    }
}
