package br.com.hitss.fieldservicemobile;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.hitss.fieldservicemobile.adapter.TicketListAdapter;
import br.com.hitss.fieldservicemobile.model.Ticket;
import br.com.hitss.fieldservicemobile.rest.TicketRestClient;
import br.com.hitss.fieldservicemobile.rest.UserRestClient;
import br.com.hitss.fieldservicemobile.thread.EnviarLocalizacaoHandlerThread;
import br.com.hitss.fieldservicemobile.thread.EnviarLocalizacaoRunnable;

/**
 * An activity representing a list of Tickets. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link TicketDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class TicketListActivity extends AppCompatActivity {

    private static final String TAG = TicketListActivity.class.getSimpleName();

    private EnviarLocalizacaoRunnable enviarLocalizacaoRunnable;
    private EnviarLocalizacaoHandlerThread enviarLocalizacaoHandlerThread;

    private static final String PREFS_NAME = "PrefsUser";

    private final TicketRestClient ticketRestClient = new TicketRestClient();
    private final UserRestClient userRestClient = new UserRestClient();

    private List<Ticket> mTickets = new ArrayList<>();

    private boolean buscarTicketsBackground = true;

    private final int delayListarTickets = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Buscando tickets...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                loadTicketList();
            }
        });
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setIcon(R.drawable.logo);
            //actionBar.setDisplayHomeAsUpEnabled(true);
        }

        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    if (buscarTicketsBackground)
                        loadTicketList();
                    try {
                        Thread.sleep(delayListarTickets);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        enviarLocalizacaoHandlerThread = new EnviarLocalizacaoHandlerThread();
        enviarLocalizacaoHandlerThread.start();
        enviarLocalizacaoRunnable = new EnviarLocalizacaoRunnable(enviarLocalizacaoHandlerThread, this);
        enviarLocalizacaoRunnable.setDelayEnvioLocalizacao(5000);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        enviarLocalizacaoRunnable.setIdUSerFsLogged(settings.getLong("idUserFsLogged", 0L));

        enviarLocalizacaoRunnable.start();
    }

    private void loadTicketList(){
        BuscaTicketsAsync buscaTicketsAsync = new BuscaTicketsAsync();
        Log.i(TAG, "AsyncTask Thread: " + Thread.currentThread().getName());
        buscaTicketsAsync.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        buscarTicketsBackground = true;
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logoff();
            enviarLocalizacaoRunnable.stop();
            enviarLocalizacaoHandlerThread.quit();
        }
        return super.onOptionsItemSelected(item);
    }

    private class BuscaTicketsAsync extends AsyncTask<String, Void, List<Ticket>> {

        @Override
        protected List<Ticket> doInBackground(String... params) {
            try {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                Long idUserFs = settings.getLong("idUserFsLogged", 0L);
                mTickets = ticketRestClient.findByidUserLogged(idUserFs);
            } catch (Exception e) {
                Log.e(TAG, "Erro ao buscar mTickets", e);
            }
            return mTickets;
        }

        @Override
        protected void onPostExecute(List<Ticket> tickets) {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            RecyclerView ticketListRecyclerView = findViewById(R.id.ticket_list_recycler_view);
            assert ticketListRecyclerView != null;
            if(mTickets == null)
                mTickets = new ArrayList<>();
            ticketListRecyclerView.setAdapter(new TicketListAdapter(TicketListActivity.this, mTickets));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ticketListRecyclerView.getContext()
                    , LinearLayoutManager.VERTICAL, false);
            ticketListRecyclerView.setLayoutManager(linearLayoutManager);

            if (tickets != null && !tickets.isEmpty()) {
                Log.i(TAG, tickets.toString());
                buscarTicketsBackground = false;
            } else {
                Boolean isUserOnTheWay = settings.getBoolean("isUserOnTheWay", false);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("isWorking", true);
                Toast.makeText(TicketListActivity.this, "Nenhum ticket encontrado.", Toast.LENGTH_LONG).show();
                Log.i(TAG, "Nenhum ticket encontrado.");
                buscarTicketsBackground = true;
            }
        }
    }

    private void logoff() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        Long idUserFs = settings.getLong("idUserFsLogged", 0L);
        LogoutTecnicoAsync logoutTecnicoAsync = new LogoutTecnicoAsync();
        Log.i(TAG, "AsyncTask Thread: " + Thread.currentThread().getName());
        logoutTecnicoAsync.execute(String.valueOf(idUserFs));
    }

    private class LogoutTecnicoAsync extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                userRestClient.postLogoff(params[0]);
                Log.i(TAG, "idUserFs: " + params[0]);
            } catch (Exception e) {
                Log.e(TAG, "Erro ao executar logoff do tecnico.", e);
                throw e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void ticket) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        logoff();
        enviarLocalizacaoRunnable.stop();
        enviarLocalizacaoHandlerThread.quit();
        super.onDestroy();
    }
}
