package br.com.hitss.fieldservicemobile;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import br.com.hitss.fieldservicemobile.model.UserFs;
import br.com.hitss.fieldservicemobile.rest.FieldserviceAPI;
import br.com.hitss.fieldservicemobile.rest.BaseController;
import br.com.hitss.fieldservicemobile.util.GPSTracker;
import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText editTextLogin;
    private EditText editTextPassword;
    private TextView textViewInfo;
    private Button buttonLogin;
    private int counter = 3;

    private static final String PREFS_NAME = "PrefsUser";

    private static final String BASE_URL = "https://fieldserviceshmg.embratel.com.br:8443/fieldservice/v1/users/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onPermision();
        setContentView(R.layout.activity_login);

        editTextLogin = findViewById(R.id.etLogin);
        editTextPassword = findViewById(R.id.etPassword);
        textViewInfo = findViewById(R.id.tvInfo);
        buttonLogin = findViewById(R.id.btnLogin);
        textViewInfo.setText("Nr de tentativas: ".concat(String.valueOf(3)));

        GPSTracker gpsTracker = new GPSTracker(LoginActivity.this);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, String> map = new HashMap<>();

                if (BuildConfig.DEBUG) {
                    map.put("login","aline.nunes.3@globalhitss.com.br");
                    map.put("password:", "1234");
                } else {
                    map.put("login",editTextLogin.getText().toString());
                    map.put("password:", editTextPassword.getText().toString());
                }
                BaseController baseController = new BaseController(BASE_URL);
                FieldserviceAPI fieldserviceAPI = baseController.getFieldserviceAPI();
                Call<UserFs> call = fieldserviceAPI.login(map);
                buttonLogin.setEnabled(false);
                call.enqueue(new retrofit2.Callback<UserFs>() {
                    @Override
                    public void onResponse(Call<UserFs> call, Response<UserFs> response) {
                        if(response.isSuccessful()) {
                            UserFs userFs = response.body();
                            Log.i(TAG, userFs.getLogin());
                            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putLong("idUserFsLogged", userFs.getIdUserFs());
                            editor.commit();
                            Intent intent = new Intent(LoginActivity.this, TicketListActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.i(TAG, "usuario não encontrado.");
                            Toast.makeText(LoginActivity.this, "usuario não encontrado.", Toast.LENGTH_LONG).show();

                            counter--;
                            textViewInfo.setText("Nr de tentativas: ".concat(String.valueOf(counter)));
                            buttonLogin.setEnabled(true);
                            if (counter == 0)
                                buttonLogin.setEnabled(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserFs> call, Throwable t) {
                        Log.i(TAG, "não encontrado.");
                        counter--;
                        textViewInfo.setText("Nr de tentativas: ".concat(String.valueOf(counter)));
                        buttonLogin.setEnabled(true);
                        if (counter == 0)
                            buttonLogin.setEnabled(false);
                    }
                });

                /*final BuscaUserAsync buscaTicketsAsync = new BuscaUserAsync();
                Log.i(TAG, "AsyncTask senado chamado Thread: " + Thread.currentThread().getName());
                buscaTicketsAsync.execute(editTextLogin.getText().toString(), editTextPassword.getText().toString());
                buttonLogin.setEnabled(false);

                //setting timeout thread for async task
                Thread thread = new Thread() {
                    public void run() {
                        try {
                            buscaTicketsAsync.get(20000, TimeUnit.MILLISECONDS);
                        } catch (Exception e) {
                            buscaTicketsAsync.cancel(true);
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                @SuppressLint("ShowToast")
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "Time Out.", Toast.LENGTH_LONG).show();
                                    buttonLogin.setEnabled(true);
                                }
                            });
                        }
                    }
                };
                thread.start();*/
            }
        });

        //TODO ficar olhando se está ativo o GPS
        if (gpsTracker.canGetLocation()){
            Toast.makeText(LoginActivity.this, "Sinal GPS encontrado.", Toast.LENGTH_LONG).show();
            buttonLogin.setEnabled(true);
            editTextLogin.setEnabled(true);
            editTextPassword.setEnabled(true);
        } else {
            Toast.makeText(LoginActivity.this, "Habilite GPS para continuar.", Toast.LENGTH_LONG).show();
            buttonLogin.setEnabled(false);
            editTextLogin.setEnabled(false);
            editTextPassword.setEnabled(false);
        }
    }

    private void onPermision() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 128);
        }
    }


    /*private class BuscaUserAsync extends AsyncTask<String, Void, UserFs> {
        @Override
        protected void onPreExecute() {
            Log.i(TAG, "Buscando user");
        }

        @Override
        protected UserFs doInBackground(String... params) {
            try {

                CustomTrust customTrust = new CustomTrust();
                customTrust.run(params[0],params[1]);

                Log.i("RETRO","TEST");
                //return new UserRestClient().login(params[0],params[1]);
                return null;
            } catch (Exception e) {
                Log.e(TAG, "Erro ao buscar tickets", e);
                //throw e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(UserFs user) {
            if (user != null) {
                Log.i(TAG, user.getLogin());
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putLong("idUserFsLogged", user.getIdUserFs());
                editor.commit();
                Intent intent = new Intent(LoginActivity.this, TicketListActivity.class);
                startActivity(intent);
                finish();
            } else {
                Log.i(TAG, "não encontrado.");
                counter--;
                textViewInfo.setText("Nr de tentativas: ".concat(String.valueOf(counter)));
                buttonLogin.setEnabled(true);
                if (counter == 0)
                    buttonLogin.setEnabled(false);
            }
        }
    }*/
}
