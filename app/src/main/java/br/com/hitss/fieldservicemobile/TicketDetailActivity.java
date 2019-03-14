package br.com.hitss.fieldservicemobile;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import br.com.hitss.fieldservicemobile.model.Ticket;
import br.com.hitss.fieldservicemobile.model.TicketHistory;
import br.com.hitss.fieldservicemobile.rest.FieldserviceAPI;
import br.com.hitss.fieldservicemobile.thread.EnviarLocalizacaoHandlerThread;
import br.com.hitss.fieldservicemobile.thread.EnviarLocalizacaoRunnable;
import br.com.hitss.fieldservicemobile.util.RetrofitHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An activity representing a single Ticket detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link TicketListActivity}.
 */
public class TicketDetailActivity extends AppCompatActivity {

    private static final String TAG = TicketDetailActivity.class.getSimpleName();

    private static final String PREFS_NAME = "PrefsUser";
    private static final String ARG_ITEM_ID = "ticket_id";
    private static final String ARG_ITEM_PARTNER_ID = "partnet_ticket_id";

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
        setSupportActionBar(toolbar);

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

        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            String partnerIdTicket = (String) extras.get(ARG_ITEM_PARTNER_ID);
            setTitle("Ticket-" + partnerIdTicket);
            String idTicket = (String) extras.get(ARG_ITEM_ID);
            findById(Long.valueOf(idTicket));
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void findById(Long idTicket) {
        final FieldserviceAPI fieldserviceAPI = RetrofitHelper.getInstance().getFieldserviceAPI();
        Call<Ticket> call = fieldserviceAPI.findById(Long.valueOf(idTicket));
        call.enqueue(new Callback<Ticket>() {
            @Override
            public void onResponse(Call<Ticket> call, Response<Ticket> response) {
                final Ticket ticket = response.body();
                if (ticket != null) {
                    Log.i(TAG, ticket.toString());
                    if (ticket != null) {
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
                        if(ticket.getSla() != null)
                            ticketSla.setText(f.format(ticket.getSla()));
                        if(ticket.getDateScheduling() != null)
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
                            final String jwt = settings.getString("jwt", null);
                            TicketHistory ticketHistory = null;
                            SharedPreferences.Editor editor = settings.edit();
                            @Override
                            public void onClick(View v) {
                                switch (ticket.getTicketStatus().getName()) {
                                    case "ASSIGNED":
                                        ticketHistory = new TicketHistory(ticket.getIdTicket(), ticket.getUserTechnician().getIdUserFs(), idUserFs, "ON_THE_WAY", "PREENCHER...");
                                        editor.putBoolean("isWorking", false);
                                        postHistoryByIdTicket(ticket.getIdTicket());
                                        break;
                                    case "ON_THE_WAY":
                                        ticketHistory = new TicketHistory(ticket.getIdTicket(), ticket.getUserTechnician().getIdUserFs(), idUserFs, "IN_PROGRESS", "PREENCHER...");
                                        editor.putBoolean("isWorking", true);
                                        EnviarLocalizacaoRunnable enviarLocalizacaoRunnable;
                                        EnviarLocalizacaoHandlerThread enviarLocalizacaoHandlerThread;
                                        postHistoryByIdTicket(ticket.getIdTicket());
                                        break;
                                    case "IN_PROGRESS":
                                        Snackbar.make(buttonTicketWorkflow,"Tem certeza que deseja encerrar ?", Snackbar.LENGTH_LONG).setAction("Sim.", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                ticketHistory = new TicketHistory(ticket.getIdTicket(), ticket.getUserTechnician().getIdUserFs(), idUserFs, "CLOSED", "PREENCHER...");
                                                editor.putBoolean("isWorking", true);
                                                postHistoryByIdTicket(ticket.getIdTicket());
                                            }
                                        }).show();
                                        break;
                                }
                                editor.apply();
                                editor.commit();

                            }

                            private void postHistoryByIdTicket(Long ticketId) {
                                Call<Void> call = fieldserviceAPI.postHistoryByIdTicket(jwt, ticketId, ticketHistory);
                                call.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        Log.i(TAG, "idTicket: " + ticket.getIdTicket());
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Log.e(TAG,t.getMessage());
                                    }
                                });
                            }
                        });
                    }
                } else {
                    Log.i(TAG, "ticket nao encontrdo.");
                }
            }

            @Override
            public void onFailure(Call<Ticket> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

}
