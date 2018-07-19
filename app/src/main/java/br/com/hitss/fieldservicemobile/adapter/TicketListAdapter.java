package br.com.hitss.fieldservicemobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.hitss.fieldservicemobile.R;
import br.com.hitss.fieldservicemobile.TicketDetailActivity;
import br.com.hitss.fieldservicemobile.TicketListActivity;
import br.com.hitss.fieldservicemobile.model.Ticket;

public class TicketListAdapter extends RecyclerView.Adapter<TicketListAdapter.ViewHolder> {

    private final List<Ticket> mValues;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Ticket item = (Ticket) view.getTag();
            Context context = view.getContext();
            Intent intent = new Intent(context, TicketDetailActivity.class);
            intent.putExtra(TicketDetailActivity.ARG_ITEM_ID, item.getIdTicket().toString());
            context.startActivity(intent);
        }
    };

    public TicketListAdapter(TicketListActivity parent, List<Ticket> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ticket_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mIdView.setText(String.valueOf(mValues.get(position).getIdTicket()));
        holder.mContentView.setText(mValues.get(position).getProblemLocalDetail());
        holder.itemView.setTag(mValues.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mIdView;
        final TextView mContentView;

        ViewHolder(View view) {
            super(view);
            mIdView = (TextView) view.findViewById(R.id.id_text);
            mContentView = (TextView) view.findViewById(R.id.content);
        }
    }
}

