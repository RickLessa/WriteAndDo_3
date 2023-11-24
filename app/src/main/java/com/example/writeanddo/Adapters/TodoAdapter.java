package com.example.writeanddo.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.writeanddo.AddNewTask;
import com.example.writeanddo.MainActivity;
import com.example.writeanddo.Model.TodoModel;
import com.example.writeanddo.R;
import com.example.writeanddo.Utils.DatabaseHandler;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private List<TodoModel> todoList;
    private DatabaseHandler db;
    private MainActivity activity;

    public TodoAdapter(DatabaseHandler db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { //cria e arranja o layout (cada item da lista)
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {//associa valores para cada "view" no item
        db.openDatabase();

        final TodoModel item = todoList.get(position);
        holder.task.setText(item.getTask()); //texto da tarefa
        holder.task.setChecked(toBoolean(item.getStatus()));//valor inicial
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // LIstener para mudança de status
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.updateStatus(item.getId(), 1);
                    Toast.makeText(activity.getApplicationContext(), "Tarefa concluída", Toast.LENGTH_SHORT).show();
                } else {
                    db.updateStatus(item.getId(), 0);
//                    Toast.makeText(activity.getApplicationContext(), "Tarefa em aberto", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean toBoolean(int n) {
        return n != 0;
    } //usado para a checklist,

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public Context getContext() {
        return activity;
    }// DB

    public void setTasks(List<TodoModel> todoList1) { //metodo para adicionar uma List qualquer ao Adpter
        this.todoList = todoList1;
        notifyDataSetChanged(); // metodo publico do adapter
    }

    public void deleteItem(int position) { //DB
        TodoModel item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) { //DB
        TodoModel item = todoList.get(position); // make not use
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder { // associa a checkbox a ao holder (cada item da lista)
        CheckBox task;                                               //app contatos

        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
        }
    }
}
