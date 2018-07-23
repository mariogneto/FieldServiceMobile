package br.com.hitss.fieldservicemobile;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import br.com.hitss.fieldservicemobile.model.Ticket;
import br.com.hitss.fieldservicemobile.model.TicketHistory;
import br.com.hitss.fieldservicemobile.rest.TicketRestClient;

/**
 * An activity representing a single Ticket detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link TicketListActivity}.
 */
public class TicketDetailActivity extends AppCompatActivity {

    private static final String TAG = TicketDetailActivity.class.getSimpleName();

    private static final String PREFS_NAME = "PrefsUser";

    public static final String ARG_ITEM_ID = "ticket_id";

    private TicketRestClient ticketRestClient = new TicketRestClient();

    private Ticket mTicket;

    private TextView ticketDescricao;
    private TextView ticketEmpresaSolicitante;

    private Button buttonTicketWorkflow;

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
        buttonTicketWorkflow = (Button) findViewById(R.id.btn_ticket_workflow);

        if (savedInstanceState == null) {
            Bundle extras = this.getIntent().getExtras();
            if (extras != null) {
                String idTicket = (String) extras.get(ARG_ITEM_ID);
                BuscaTicketAsync buscaTicketsAsync = new BuscaTicketAsync();
                Log.i(TAG, "AsyncTask Thread: " + Thread.currentThread().getName());
                buscaTicketsAsync.execute(idTicket);
            }
        } else {
            String idTicket = (String) savedInstanceState.getSerializable(ARG_ITEM_ID);
            BuscaTicketAsync buscaTicketsAsync = new BuscaTicketAsync();
            Log.i(TAG, "AsyncTask Thread: " + Thread.currentThread().getName());
            buscaTicketsAsync.execute(idTicket);
        }
    }

    private class BuscaTicketAsync extends AsyncTask<String, Void, Ticket> {
        @Override
        protected Ticket doInBackground(String... params) {
            try {
                mTicket = ticketRestClient.findById(Long.valueOf(params[0]));
            } catch (Exception e) {
                Log.e(TAG, "Erro ao buscar ticket", e);
            }
            return mTicket;
        }

        @Override
        protected void onPostExecute(Ticket ticket) {
            super.onPostExecute(ticket);
            if (mTicket != null) {
                Log.i(TAG, mTicket.toString());
                if (mTicket != null) {
                    setTitle(mTicket.getPartnerTicketCode());
                    ticketDescricao.setText(mTicket.getProblemDescription());
                    ticketEmpresaSolicitante.setText(mTicket.getUserAffected().getLocation().getCustomer().getName());

                    //muda texto do botao
                    switch (mTicket.getTicketStatus().getName()) {
                        case "ASSIGNED":
                            buttonTicketWorkflow.setText("A CAMINHO");
                            break;
                        case "ON_THE_WAY":
                            buttonTicketWorkflow.setText("TRABALHAR");
                            break;
                        case "IN_PROGRESS":
                            buttonTicketWorkflow.setText("RESOLVER");
                            break;
                        case "CLOSED":
                            buttonTicketWorkflow.setVisibility(View.INVISIBLE);
                            break;
                    }

                    buttonTicketWorkflow.setOnClickListener(new View.OnClickListener() {
                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                        Long idUserFs = settings.getLong("idUserFsLogged", 0L);
                        @Override
                        public void onClick(View v) {
                            PostHistoryByIdTicketAsync postHistoryByIdTicketAsync = new PostHistoryByIdTicketAsync();
                            Log.i(TAG, "AsyncTask Thread: " + Thread.currentThread().getName());
                            SharedPreferences.Editor editor = settings.edit();
                            switch (mTicket.getTicketStatus().getName()) {
                                case "ASSIGNED":
                                    postHistoryByIdTicketAsync.execute(new TicketHistory(mTicket.getIdTicket(), mTicket.getUserTechnician().getIdUserFs(), idUserFs, "ON_THE_WAY", "PREENCHER..."));
                                    editor.putBoolean("isWorking", false);
                                    break;
                                case "ON_THE_WAY":
                                    postHistoryByIdTicketAsync.execute(new TicketHistory(mTicket.getIdTicket(), mTicket.getUserTechnician().getIdUserFs(), idUserFs, "IN_PROGRESS", "PREENCHER..."));
                                    editor.putBoolean("isWorking", true);
                                    break;
                                case "IN_PROGRESS":
                                    postHistoryByIdTicketAsync.execute(new TicketHistory(mTicket.getIdTicket(), mTicket.getUserTechnician().getIdUserFs(), idUserFs, "CLOSED", "PREENCHER..."));
                                    editor.putBoolean("isWorking", true);
                                    break;
                              default:
                                  editor.commit();
                            }
                        }
                    });
                }
            } else {
                Log.i(TAG, "vazio!");
            }
        }
    }

    private class PostHistoryByIdTicketAsync extends AsyncTask<TicketHistory, Void, Void> {

        @Override
        protected Void doInBackground(TicketHistory... params) {
            try {
                ticketRestClient.postHistoryByIdTicket(params[0]);
                Log.i(TAG, "idTicket: " + params[0].getIdTicket());
            } catch (Exception e) {
                Log.e(TAG, "Erro ao executar alteracao de status do ticket.", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            finish();
            super.onPostExecute(aVoid);
        }
    }

}
