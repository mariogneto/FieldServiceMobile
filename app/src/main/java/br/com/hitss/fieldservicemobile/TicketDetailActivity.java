package br.com.hitss.fieldservicemobile;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.TextView;

import br.com.hitss.fieldservicemobile.model.Ticket;
import br.com.hitss.fieldservicemobile.rest.TicketRestClient;

/**
 * An activity representing a single Ticket detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link TicketListActivity}.
 */
public class TicketDetailActivity extends AppCompatActivity {

    public static final String ARG_ITEM_ID = "ticket_id";

    private TicketRestClient ticketRestClient = new TicketRestClient();

    private Ticket mTicket;

    private TextView ticketDescricao;
    private TextView ticketEmpresaSolicitante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        toolbar.setSubtitle("SubTest");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Substitua com sua própria ação de detalhes", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ticketDescricao = (TextView) findViewById(R.id.ticket_descricao);
        ticketEmpresaSolicitante = (TextView) findViewById(R.id.ticket_empresa_solicitante);

        if (savedInstanceState == null) {
            Bundle extras = this.getIntent().getExtras();
            if (extras != null) {
                String idTicket = (String) extras.get(ARG_ITEM_ID);
                BuscaTicketAsync buscaTicketsAsync = new BuscaTicketAsync();
                Log.i("AsyncTask", "AsyncTask Thread: " + Thread.currentThread().getName());
                buscaTicketsAsync.execute(idTicket);
            }
        } else {
            String idTicket = (String) savedInstanceState.getSerializable(ARG_ITEM_ID);
            BuscaTicketAsync buscaTicketsAsync = new BuscaTicketAsync();
            Log.i("AsyncTask", "AsyncTask Thread: " + Thread.currentThread().getName());
            buscaTicketsAsync.execute(idTicket);
        }
    }

    private class BuscaTicketAsync extends AsyncTask<String, Void, Ticket> {
        @Override
        protected Ticket doInBackground(String... params) {
            try {
                mTicket = ticketRestClient.findById(Long.valueOf(params[0]));
            } catch (Exception e) {
                Log.e("tickets", "Erro ao buscar ticket", e);
            }
            return mTicket;
        }

        @Override
        protected void onPostExecute(Ticket ticket) {
            super.onPostExecute(ticket);
            if (mTicket != null) {
                Log.i("ticket", mTicket.toString());
                if (mTicket != null) {
                    ticketDescricao.setText(mTicket.getProblemDescription());
                    ticketEmpresaSolicitante.setText(mTicket.getUserAffected().getLocation().getCustomer().getName());
                }
            } else {
                Log.i("ticket", "vazio!");
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, TicketListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
