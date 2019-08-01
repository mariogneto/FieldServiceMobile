package br.com.hitss.fieldservicemobile;

import android.content.SharedPreferences;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


import br.com.hitss.fieldservicemobile.adapter.TicketListAdapter;
import br.com.hitss.fieldservicemobile.model.Ticket;
import br.com.hitss.fieldservicemobile.rest.FieldserviceAPI;
import br.com.hitss.fieldservicemobile.thread.EnviarLocalizacaoHandlerThread;
import br.com.hitss.fieldservicemobile.thread.EnviarLocalizacaoRunnable;
import br.com.hitss.fieldservicemobile.util.RetrofitHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public EnviarLocalizacaoRunnable enviarLocalizacaoRunnable;
    public EnviarLocalizacaoHandlerThread enviarLocalizacaoHandlerThread;

    private static final String PREFS_NAME = "PrefsUser";

    private List<Ticket> mTicketsResolved = new ArrayList<>();
    private List<Ticket> mTicketsWork = new ArrayList<>();
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
            actionBar.setIcon(R.drawable.logo_inicio);
        }

        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    if (buscarTicketsBackground) {
                        loadTicketList();
                    }
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

    private void loadTicketList() {

        final FieldserviceAPI fieldserviceAPI = RetrofitHelper.getInstance().getFieldserviceAPI();

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Calendar cal = Calendar.getInstance();

        String endDate = df.format(cal.getTime());

        cal.add(Calendar.DATE, -100);
        String startDate = df.format(cal.getTime());

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        Long idUserFs = settings.getLong("idUserFsLogged", 0L);

        mTickets.clear();

        Call<List<Ticket>> call = fieldserviceAPI.findByidUserLogged(idUserFs, "2,3,4,7");
        call.enqueue(new Callback<List<Ticket>>() {
            @Override
            public void onResponse(Call<List<Ticket>> call, Response<List<Ticket>> response) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

                if (response.isSuccessful()) {
                    mTicketsWork = response.body();
                    if (mTicketsWork != null && !mTicketsWork.isEmpty()) {
                        mTickets.addAll(mTicketsWork);
                    }
                }

                if (mTicketsWork != null && !mTicketsWork.isEmpty()) {
                    Log.i(TAG, response.toString());
                    buscarTicketsBackground = false;
                } else {
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("isWorking", true);
                    Log.i(TAG, "Nenhum ticket encontrado com idStatus 2,3,4 e 7.");
                    buscarTicketsBackground = true;
                }
            }

            @Override
            public void onFailure(Call<List<Ticket>> call, Throwable t) {
                Log.e(TAG, "erro ao carregar tickets: " + t.getMessage());
            }
        });

        Call<List<Ticket>> callResolved = fieldserviceAPI.findByidUserLoggedDate(idUserFs, "5", startDate, endDate);
        callResolved.enqueue(new Callback<List<Ticket>>() {
            @Override
            public void onResponse(Call<List<Ticket>> callResolved, Response<List<Ticket>> responseResolved) {
                RecyclerView ticketListRecyclerView = findViewById(R.id.ticket_list_recycler_view);
                assert ticketListRecyclerView != null;
                if (responseResolved.isSuccessful()) {
                    mTicketsResolved = responseResolved.body();
                    if (mTicketsResolved != null && !mTicketsResolved.isEmpty()){
                        Collections.reverse(mTicketsResolved);
                        mTickets.addAll(mTicketsResolved);
                    }
                    if (mTickets == null) {
                        mTickets = new ArrayList<>();
                    }
                }

                ticketListRecyclerView.setAdapter(new TicketListAdapter(TicketListActivity.this, mTickets));
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ticketListRecyclerView.getContext()
                        , LinearLayoutManager.VERTICAL, false);
                ticketListRecyclerView.setLayoutManager(linearLayoutManager);
            }
            @Override
            public void onFailure(Call<List<Ticket>> callResolved, Throwable t) {
                Log.e(TAG, "erro ao carregar tickets com status RESOLVIDO: " + t.getMessage());
            }
        });


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
    public void onBackPressed() {
        moveTaskToBack(true);
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

    private void logoff() {
        final FieldserviceAPI fieldserviceAPI = RetrofitHelper.getInstance().getFieldserviceAPI();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        Long idUserFs = settings.getLong("idUserFsLogged", 0L);
        Call<Void> call = fieldserviceAPI.postLogoff(idUserFs);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful())
                    Log.i(TAG, "Logoff concluido com sucesso.");
                enviarLocalizacaoRunnable.stop();
                enviarLocalizacaoHandlerThread.quit();
                buscarTicketsBackground = false;
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    @Override
    protected void onDestroy() {
        enviarLocalizacaoRunnable.stop();
        enviarLocalizacaoHandlerThread.quit();
        buscarTicketsBackground = false;
        super.onDestroy();
    }
}
