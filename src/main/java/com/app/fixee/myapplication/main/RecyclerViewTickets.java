package com.app.fixee.myapplication.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.fixee.myapplication.R;
import com.app.fixee.myapplication.callbacks.OnResponseClickTicket;
import com.app.fixee.myapplication.models.Ticket;
import com.bumptech.glide.Glide;

import java.util.List;

public class RecyclerViewTickets extends RecyclerView.Adapter<RecyclerViewTickets.TicketsViewHolder> {

    private static final String status4 = "Выполнено";
    private View view;

    private List<Ticket> tickets;
    private int accessLevel;
    private OnResponseClickTicket callback;

    RecyclerViewTickets(List<Ticket> tickets, int accessLevel, OnResponseClickTicket callback) {
        this.tickets = tickets;
        this.accessLevel = accessLevel;
        this.callback = callback;
    }

    @Override
    public TicketsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ticket_user, parent, false);
        return new TicketsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TicketsViewHolder holder, int position) {
        holder.name.setText(tickets.get(position).getName());
        holder.description.setText(tickets.get(position).getDescription());
        holder.builder.setText(tickets.get(position).getBuilder());
        holder.date.setText(tickets.get(position).getDate());
        holder.status.setText(tickets.get(position).getStatus());

        if (!tickets.get(position).getPhoto().isEmpty()) {

            holder.imageView.setVisibility(View.VISIBLE);

            Glide.with(view)
                    .load(tickets.get(position).getPhoto())
                    .into(holder.imageView);
        }

        if (accessLevel > 1) {
            holder.linearLayout.setOnClickListener(view -> callback.onSuccess(tickets.get(position), position));
        } else if (accessLevel == 1 && tickets.get(position).getStatus().equals(status4) && tickets.get(position).getFeedback().isEmpty()) {
            holder.linearLayout.setOnClickListener(view1 -> callback.onSuccessFeedback(tickets.get(position).getDocId()));
        }
    }

    @Override
    public int getItemCount() {
        return tickets.isEmpty() ? 0 : tickets.size();
    }

    public static class TicketsViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        TextView name;
        TextView description;
        TextView builder;
        TextView date;
        TextView status;
        ImageView imageView;

        public TicketsViewHolder(View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.linear_layout);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            builder = itemView.findViewById(R.id.builder);
            date = itemView.findViewById(R.id.date);
            status = itemView.findViewById(R.id.status);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
