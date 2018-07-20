package br.com.hitss.fieldservicemobile;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import br.com.hitss.fieldservicemobile.adapter.TicketListAdapter;
import br.com.hitss.fieldservicemobile.model.Ticket;
import br.com.hitss.fieldservicemobile.rest.TicketRestClient;
import br.com.hitss.fieldservicemobile.rest.UserRestClient;
import br.com.hitss.fieldservicemobile.thread.Client;
import br.com.hitss.fieldservicemobile.thread.PostOfficeHandlerThread;
import br.com.hitss.fieldservicemobile.thread.SimulatorRunnable;
import br.com.hitss.fieldservicemobile.util.GPSTracker;

/**
 * An activity representing a list of Tickets. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link TicketDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class TicketListActivity extends AppCompatActivity implements Client.ClientCallback  {


    private SimulatorRunnable mSimulator;
    private PostOfficeHandlerThread mPostOffice;

    private static final String PREFS_NAME = "PrefsUser";

    private TicketRestClient ticketRestClient = new TicketRestClient();
    private UserRestClient userRestClient = new UserRestClient();

    private List<Ticket> mTickets = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        BuscaTicketsAsync buscaTicketsAsync = new BuscaTicketsAsync();
        Log.i("AsyncTask", "AsyncTask Thread: " + Thread.currentThread().getName());
        buscaTicketsAsync.execute();

        enviaLocalizacaoUsuarioLogado();

        mPostOffice = new PostOfficeHandlerThread(new LinkedHashMap<Integer, Handler>());
        mPostOffice.start();
        mSimulator = new SimulatorRunnable(mPostOffice, this);
        mSimulator.createClients(10).start();

    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    public void onNewPost(final Client receiver, final Client sender, final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
               /* int position = mPostListAdapter.getFeedItemList().size();
                mPostListAdapter.getFeedItemList()
                        .add(new PostListAdapter.FeedItem(sender.getName(), receiver.getName(), message));
                mPostFeedView.getAdapter().notifyItemInserted(position);
                mPostFeedView.smoothScrollToPosition(position);*/
                Toast.makeText(TicketListActivity.this, "THREAD...", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void enviaLocalizacaoUsuarioLogado() {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TicketListActivity.this, "THREAD...", Toast.LENGTH_LONG).show();
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                Long idUserFs = settings.getLong("idUserFsLogger", 0L);
                EnviaPosicaoTecnicoAsync enviaPosicaoTecnicoAsync = new EnviaPosicaoTecnicoAsync();
                Log.i("AsyncTask", "AsyncTask Thread: " + Thread.currentThread().getName());
                GPSTracker gpsTracker = new GPSTracker(TicketListActivity.this);
                Location location = gpsTracker.getLocation();
                enviaPosicaoTecnicoAsync.execute(String.valueOf(idUserFs), String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
            }
        }, 3000);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new TicketListAdapter(this , mTickets));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {

        mSimulator.stop();
        mPostOffice.quit();



        logoff();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logoff();
        }
        return super.onOptionsItemSelected(item);
    }

    private class BuscaTicketsAsync extends AsyncTask<String, Void, List<Ticket>> {

        @Override
        protected List<Ticket> doInBackground(String... params) {
            try {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                Long idUserFs = settings.getLong("idUserFsLogger", 0L);
                mTickets = ticketRestClient.findByidUserLogged(idUserFs);
            } catch (Exception e) {
                Log.e("mTickets", "Erro ao buscar mTickets", e);
            }
            return mTickets;
        }

        @Override
        protected void onPostExecute(List<Ticket> tickets) {
            if (tickets != null && !tickets.isEmpty()) {
                Log.i("mTickets", tickets.toString());
                View recyclerView = findViewById(R.id.ticket_list);
                assert recyclerView != null;
                setupRecyclerView((RecyclerView) recyclerView);
            } else {
                Toast.makeText(TicketListActivity.this, "Nenhum ticket encontrado.", Toast.LENGTH_LONG).show();
                Log.i("mTickets", "Nenhum ticket encontrado.");
            }
        }
    }

    private class EnviaPosicaoTecnicoAsync extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                userRestClient.postUserLocationHistory(params[0], Double.valueOf(params[1]), Double.valueOf(params[2]));
                Log.i("GPS", "idUserFs: "+ params[0] +"  latitude: " + params[1] + "longitude: " + params[2]);
            } catch (Exception e) {
                Log.e("GPS", "Erro ao enviar posicao tecnico.", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void ticket) {
            Toast.makeText(TicketListActivity.this, "Envio de posicao", Toast.LENGTH_SHORT).show();
        }
    }

    private void logoff() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        Long idUserFs = settings.getLong("idUserFsLogger", 0L);
        LogoutTecnicoAsync logoutTecnicoAsync = new LogoutTecnicoAsync();
        Log.i("AsyncTask", "AsyncTask Thread: " + Thread.currentThread().getName());
        logoutTecnicoAsync.execute(String.valueOf(idUserFs));
    }

    private class LogoutTecnicoAsync extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                userRestClient.postLogoff(params[0]);
                Log.i("LOGOUT", "idUserFs: "+ params[0]);
            } catch (Exception e) {
                Log.e("GPS", "Erro ao executar logoff do tecnico.", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void ticket) {
            Toast.makeText(TicketListActivity.this, "Fechando Sistema", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

}
