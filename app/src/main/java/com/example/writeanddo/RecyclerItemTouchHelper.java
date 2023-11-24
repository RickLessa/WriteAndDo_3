package com.example.writeanddo;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.writeanddo.Adapters.TodoAdapter;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private TodoAdapter adapter;

    public RecyclerItemTouchHelper(TodoAdapter adapter) {
        super(0, ItemTouchHelper.LEFT);
        this.adapter = adapter;
    }


    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.LEFT) {
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
            builder.setTitle("Deletar tarefa");
            builder.setMessage("Tem certeza que deseja deletar a tarefa? ");
            builder.setPositiveButton("Confirmar",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.deleteItem(position);
                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        Drawable icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_baseline_delete);
        ColorDrawable background = new ColorDrawable(Color.RED);

        View itemView = viewHolder.itemView;

        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();



        int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
        int iconRight = itemView.getRight() - iconMargin;
        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

        background.setBounds(itemView.getRight() + ((int) dX), itemView.getTop(), itemView.getRight(), itemView.getBottom());



        background.draw(c);
        icon.draw(c);
    }
}
