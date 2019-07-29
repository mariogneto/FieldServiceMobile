package br.com.hitss.fieldservicemobile;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
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

import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import br.com.hitss.fieldservicemobile.model.UserFs;
import br.com.hitss.fieldservicemobile.model.UserLocationHistory;
import br.com.hitss.fieldservicemobile.pushnotification.NotificationOpenedHandler;
import br.com.hitss.fieldservicemobile.rest.FieldserviceAPI;
import br.com.hitss.fieldservicemobile.util.RetrofitHelper;
import br.com.hitss.fieldservicemobile.util.GPSTracker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText editTextLogin;
    private EditText editTextPassword;
    private TextView textViewInfo;
    private Button buttonLogin;
    private int counter = 3;

    private static final String PREFS_NAME = "PrefsUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // OneSignal Initialization
        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new NotificationOpenedHandler(getApplication()))
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

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

                buttonLogin.setEnabled(false);

                final FieldserviceAPI fieldserviceAPI = RetrofitHelper.getInstance().getFieldserviceAPI();

                if (!editTextLogin.getText().toString().isEmpty() && !editTextPassword.getText().toString().isEmpty()) {
                    Map<String, String> map = new HashMap<>();
                    map.put("login", editTextLogin.getText().toString());
                    map.put("password:", editTextPassword.getText().toString());
                    Call<UserFs> call = fieldserviceAPI.login(map);
                    call.enqueue(new Callback<UserFs>() {
                        @Override
                        public void onResponse(Call<UserFs> call, Response<UserFs> response) {
                            if (response.isSuccessful()) {
                                UserFs userFs = response.body();
                                Log.i(TAG, userFs.getLogin());

                                FieldserviceAPI fieldserviceAPI = RetrofitHelper.getInstance().getFieldserviceAPI();
                                GPSTracker gpsTracker = new GPSTracker(LoginActivity.this);
                                Location location = gpsTracker.getLocation();
                                UserLocationHistory userLocationHistory = new UserLocationHistory(location.getLatitude(), location.getLongitude());

                                Call<Void> call2 = fieldserviceAPI.postUserLocationHistory(userFs.getIdUserFs(), userLocationHistory);
                                call2.enqueue(new retrofit2.Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if (response.isSuccessful()) {
                                            Log.i(TAG, "Envio de localização após login feita com sucesso.");
                                        } else {
                                            onFailure(call, new Throwable());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Log.e(TAG, "Erro ao enviar posicao tecnico.", t);
                                    }
                                });


                                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putLong("idUserFsLogged", userFs.getIdUserFs());
                                editor.commit();
                                Intent intent = new Intent(LoginActivity.this, TicketListActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Log.i(TAG, "usuario ou senha não conferem.");
                                Toast.makeText(LoginActivity.this, "usuario ou senha não conferem.", Toast.LENGTH_LONG).show();

                                counter--;
                                textViewInfo.setText("Nr de tentativas: ".concat(String.valueOf(counter)));
                                buttonLogin.setEnabled(true);
                                if (counter == 0) {
                                    buttonLogin.setEnabled(false);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserFs> call, Throwable t) {
                            Log.e(TAG, "ERRO: " + t.getMessage());
                            counter--;
                            textViewInfo.setText("Nr de tentativas: ".concat(String.valueOf(counter)));
                            buttonLogin.setEnabled(true);
                            if (counter == 0)
                                buttonLogin.setEnabled(false);
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Preencha todos os campos.", Toast.LENGTH_LONG).show();
                    buttonLogin.setEnabled(true);
                }
            }
        });

        //TODO ficar olhando se está ativo o GPS
        if (gpsTracker.canGetLocation()) {
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

}
