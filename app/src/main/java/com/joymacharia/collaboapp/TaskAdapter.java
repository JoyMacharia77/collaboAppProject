package com.joymacharia.collaboapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter <TaskAdapter.ViewHolder> {
    private ArrayList<Task> taskData;
    private Context myContext;

    TaskAdapter(ArrayList<Task> taskData,Context context)
    {
        this.myContext = context;
        this.taskData = taskData;
    }

    @NonNull
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(myContext).inflate(R.layout.task_list_item, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder holder, int position) {
        Task currentTask = taskData.get(position);

        holder.bindTo(currentTask);
    }

    @Override
    public int getItemCount()
    {
        return taskData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView myTaskImage;
        private TextView myTaskTitle;
        private TextView myTaskDeadline;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            myTaskImage = itemView.findViewById(R.id.task_image);
            myTaskTitle = itemView.findViewById(R.id.task_title);
            myTaskDeadline = itemView.findViewById(R.id.task_deadline);

            //onclick of items
           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int taskPosition = getAdapterPosition();
                    Task currentTask = taskData.get(taskPosition);


                }
            });*/

        }

        public void bindTo(Task currentTask)
        {
            Glide.with(myContext).load(currentTask.getTaskImage()).into(myTaskImage);
            myTaskTitle.setText(currentTask.getTaskTitle());
            myTaskDeadline.setText(currentTask.getTaskDeadline());
        }
    }
}
