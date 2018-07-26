package br.com.hitss.fieldservicemobile;

import android.content.SharedPreferences;
import android.graphics.Color;
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

import java.text.SimpleDateFormat;
import java.util.Locale;

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

    private TextView ticketDescricao;
    private TextView ticketPartnerCode;
    private TextView ticketEmpresaSolicitante;
    private TextView ticketSla;
    private TextView ticketEndereco;
    private TextView ticketResponsavel;
    private TextView ticketDataAgendamento;

    private Button buttonTicketWorkflow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        toolbar.setSubtitle("SubTest");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
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

        ticketDataAgendamento = findViewById(R.id.ticket_data_agendamento);
        ticketEndereco = findViewById(R.id.ticket_endereco);
        ticketPartnerCode = findViewById(R.id.ticket_partner_code);
        ticketResponsavel = findViewById(R.id.ticket_responsavel);
        ticketDescricao = findViewById(R.id.ticket_descricao);
        ticketEmpresaSolicitante = findViewById(R.id.ticket_empresa_solicitante);
        ticketSla = findViewById(R.id.ticket_sla);
        buttonTicketWorkflow = findViewById(R.id.btn_ticket_workflow);

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
                return new TicketRestClient().findById(Long.valueOf(params[0]));
            } catch (Exception e) {
                Log.e(TAG, "Erro ao buscar ticket.", e);
                throw e;
            }
        }

        @Override
        protected void onPostExecute(final Ticket ticket) {
            super.onPostExecute(ticket);
            if (ticket != null) {
                Log.i(TAG, ticket.toString());
                if (ticket != null) {
                    setTitle(ticket.getPartnerTicketCode());
                    ticketPartnerCode.setText(ticket.getPartnerTicketCode());
                    ticketDescricao.setText(ticket.getProblemDescription());
                    String ticketEnderecoText = ticket.getUserAffected().getLocation().getAddress() + "," +
                            ticket.getUserAffected().getLocation().getNumber() + " - " +
                            ticket.getUserAffected().getLocation().getNeighborhood() + ", " +
                            ticket.getUserAffected().getLocation().getCity() + "-" + ticket.getUserAffected().getLocation().getState() + " " + ticket.getUserAffected().getLocation().getZipCode();
                    ticketEndereco.setText(ticketEnderecoText);
                    ticketResponsavel.setText(ticket.getUserAffected().getFullName());
                    String ticketEmpresaSolicitanteText = ticket.getUserAffected().getLocation().getCustomer().getName() + " - " +
                            ticket.getUserAffected().getLocation().getName();
                    ticketEmpresaSolicitante.setText(ticketEmpresaSolicitanteText);
                    SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm", new Locale("pt","BR"));
                    ticketSla.setText(f.format(ticket.getSla()));
                    ticketDataAgendamento.setText(f.format(ticket.getDateScheduling()));

                    switch (ticket.getTicketStatus().getName()) {
                        case "ON_THE_WAY":
                            buttonTicketWorkflow.setText("TRABALHAR");
                            buttonTicketWorkflow.setBackgroundColor(Color.parseColor("#32CD32"));
                            buttonTicketWorkflow.setVisibility(View.VISIBLE);
                            break;
                        case "IN_PROGRESS":
                            buttonTicketWorkflow.setText("RESOLVER");
                            buttonTicketWorkflow.setBackgroundColor(Color.parseColor("#FF0000"));
                            buttonTicketWorkflow.setVisibility(View.VISIBLE);
                            break;
                        default:
                            buttonTicketWorkflow.setVisibility(View.INVISIBLE);
                            break;
                    }

                    buttonTicketWorkflow.setOnClickListener(new View.OnClickListener() {
                        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                        final Long idUserFs = settings.getLong("idUserFsLogged", 0L);
                        @Override
                        public void onClick(View v) {
                            PostHistoryByIdTicketAsync postHistoryByIdTicketAsync = new PostHistoryByIdTicketAsync();
                            Log.i(TAG, "AsyncTask Thread: " + Thread.currentThread().getName());
                            SharedPreferences.Editor editor = settings.edit();
                            switch (ticket.getTicketStatus().getName()) {
                                case "ASSIGNED":
                                    postHistoryByIdTicketAsync.execute(new TicketHistory(ticket.getIdTicket(), ticket.getUserTechnician().getIdUserFs(), idUserFs, "ON_THE_WAY", "PREENCHER..."));
                                    editor.putBoolean("isWorking", false);
                                    break;
                                case "ON_THE_WAY":
                                    postHistoryByIdTicketAsync.execute(new TicketHistory(ticket.getIdTicket(), ticket.getUserTechnician().getIdUserFs(), idUserFs, "IN_PROGRESS", "PREENCHER..."));
                                    editor.putBoolean("isWorking", true);
                                    break;
                                case "IN_PROGRESS":
                                    postHistoryByIdTicketAsync.execute(new TicketHistory(ticket.getIdTicket(), ticket.getUserTechnician().getIdUserFs(), idUserFs, "CLOSED", "PREENCHER..."));
                                    editor.putBoolean("isWorking", true);
                                    break;
                              default:
                                  editor.apply();
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
                new TicketRestClient().postHistoryByIdTicket(params[0]);
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
