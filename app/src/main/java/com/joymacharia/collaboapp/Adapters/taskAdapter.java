package com.joymacharia.collaboapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joymacharia.collaboapp.R;
import com.joymacharia.collaboapp.constants;
import com.joymacharia.collaboapp.models.Task;
import com.joymacharia.collaboapp.models.newTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class taskAdapter extends RecyclerView.Adapter<taskAdapter.taskHolder> {

    private Context context;
    private ArrayList<newTask> list;

    public taskAdapter(Context context, ArrayList<newTask> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public taskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_view_tasks,parent,false);
    return new taskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull taskHolder holder, int position) {
        newTask task = list.get(position);
        Picasso.get().load(constants.URL+"storage/taskImages/"+task.getUser().getPhoto()).into((Target) holder.taskImage);

        holder.taskName.setText(task.getTaskTitle());
        holder. taskDeadline.setText(task.getTaskDeadline());
     }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class taskHolder extends RecyclerView.ViewHolder{
        private TextView taskName, taskDeadline,taskImage;
        private Button addTask;

        public taskHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.task);
            taskDeadline = itemView.findViewById(R.id.task_deadline);
            addTask = itemView.findViewById(R.id.add_btn);
            taskImage = itemView.findViewById(R.id.task_image);

        }
    }
}
