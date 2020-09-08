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
public class TimerFragment extends Fragment {

    private Button btnStopWatch;
    private View view;

    public TimerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_timer, container, false);
        init();
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_new_add_task, container, false);
        return view;
    }

    private void init()
    {
        btnStopWatch = view.findViewById(R.id.btnStopWatch);

        btnStopWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent watch = new Intent(getContext(), ClockActivity.class);
                startActivity(watch);
            }
        });
    }
}
