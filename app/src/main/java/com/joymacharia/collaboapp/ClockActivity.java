package com.joymacharia.collaboapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class ClockActivity extends AppCompatActivity {

    private int seconds = 0;
    private boolean running;
    private boolean wasRunning; // records whether stopwatch was running before onStop()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        if(savedInstanceState != null)
        {
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }
        runTimer();
    }


    @Override
    /* enables you to save your activity's state before it gets destroyed  */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // this.savedInstanceState = savedInstanceState;
        super.onSaveInstanceState(savedInstanceState);
        /* format is bundle.put*("name","value") where
         * bundle is name of bundle, *is value type
         * name and value are those of the data */
        savedInstanceState.putInt("seconds", seconds);
        savedInstanceState.putBoolean("running", running);
        savedInstanceState.putBoolean("wasRunning", wasRunning);
    }

    @Override
    //If activity is paused,stop the stopwatch
    protected void onPause()
    {
        super.onPause();
        //Record whether the stopwatch was running when the onPause() method was called
        wasRunning = running;
        running = false;
    }
    @Override
    /*  if activity is resumed,start stopwatch again if it was previously running */
    protected void onResume()
    {
        super.onResume();
        //implement the onResume()method if the stopwatch was running,set it running again
        if(wasRunning)
        {
            running = true;
        }
    }


    //REPLACE onStop() and onStart() with onPause() and onResume()

    //Start Stopwatch
    public void onClickStart(View view)
    {
        running = true;
    }

    public void onClickStop(View view)
    {
        running = false;
    }

    public void onClickReset(View view)
    {
        running = false;
        seconds = 0;
    }

    private void runTimer()
    {
        final TextView timeView = (TextView) findViewById(R.id.time_view);
        //CREATE NEW EVENT HANDLER
        final Handler handler;
        handler = new Handler();
        /* call the post()method passing a new runnable,which runs almost immediately,
          since post() processes code without delay*/
        handler.post(new Runnable() {
            @Override
            /* The Runnable run()method contains the code you want to be run
             * in this case.code to update text view*/
            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds%3600) / 60;
                int secs = seconds % 60;
                /* format seconds to hrs,min and secs */
                String time = String.format(getString(R.string.time), hours, minutes, secs);
                timeView.setText(time);
                if (running)
                {
                    seconds++;
                }
                /* posts code in runnable to be run again after a delay of 1000millis
                 * or one second */
                handler.postDelayed(this, 1000);
            }
        });
    }
}
