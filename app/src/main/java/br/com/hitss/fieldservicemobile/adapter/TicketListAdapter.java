package br.com.hitss.fieldservicemobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

    private static final String ARG_ITEM_ID = "ticket_id";
    private static final String ARG_ITEM_PARTNER_ID = "partnet_ticket_id";

    private final List<Ticket> mTickets;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Ticket item = (Ticket) view.getTag();
            Context context = view.getContext();
            Intent intent = new Intent(context, TicketDetailActivity.class);
            intent.putExtra(ARG_ITEM_ID, item.getIdTicket().toString());
            intent.putExtra(ARG_ITEM_PARTNER_ID, item.getPartnerTicketCode());
            context.startActivity(intent);
        }
    };

    public TicketListAdapter(TicketListActivity parent, List<Ticket> tickets) {
        mTickets = tickets;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ticket_list_content, parent, false);
        //sparent.setBackgroundColor(Color.parseColor("#3366FF"));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mPartnerTicket.setText(String.valueOf(mTickets.get(position).getPartnerTicketCode()));
        holder.mDetail.setText(mTickets.get(position).getUserAffected().getLocation().getCustomer().getName());

        switch (mTickets.get(position).getTicketStatus().getName()) {
            case "ASSIGNED":
                holder.mStatus.setText("ATRIBUIDO");
                holder.mStatus.setBackgroundColor(Color.parseColor("#FFFF00"));
                break;
            case "ON_THE_WAY":
                holder.mStatus.setText("A CAMINHO");
                holder.mStatus.setBackgroundColor(Color.parseColor("#1E90FF"));
                break;
            case "IN_PROGRESS":
                holder.mStatus.setText("TRABALHANDO");
                holder.mStatus.setBackgroundColor(Color.parseColor("#32CD32"));
                break;
            default:
                holder.mStatus.setBackgroundColor(Color.parseColor("#D3D3D3"));
                break;
        }
        holder.itemView.setTag(mTickets.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mTickets.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mPartnerTicket;
        final TextView mDetail;
        final TextView mStatus;

        ViewHolder(View view) {
            super(view);
            mPartnerTicket = view.findViewById(R.id.ticket_list_partner_ticket);
            mDetail = view.findViewById(R.id.ticket_list_detail);
            mStatus = view.findViewById(R.id.ticket_list_status);
        }
    }
}