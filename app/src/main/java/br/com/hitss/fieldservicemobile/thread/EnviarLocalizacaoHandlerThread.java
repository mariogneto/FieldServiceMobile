package br.com.hitss.fieldservicemobile.thread;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import br.com.hitss.fieldservicemobile.model.UserLocationHistory;
import br.com.hitss.fieldservicemobile.rest.BaseController;
import br.com.hitss.fieldservicemobile.rest.FieldserviceAPI;
import br.com.hitss.fieldservicemobile.util.GPSTracker;
import retrofit2.Call;
import retrofit2.Response;

public class EnviarLocalizacaoHandlerThread extends HandlerThread {

    private static final String TAG = EnviarLocalizacaoHandlerThread.class.getSimpleName();

    private static final int times = 3;

    public EnviarLocalizacaoHandlerThread() {
        super(TAG);
    }

    private static final String BASE_URL = "https://fieldserviceshmg.embratel.com.br:8443/fieldservice/v1/";

    private int count = 0;
    public synchronized void sendLocation(final Long idUserFsLogged, final Context context) {

        Handler handler =
                new Handler(getLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        BaseController baseController = new BaseController(BASE_URL);
                        FieldserviceAPI fieldserviceAPI = baseController.getFieldserviceAPI();
                        GPSTracker gpsTracker = new GPSTracker(context);
                        final Location location = gpsTracker.getLocation();
                        UserLocationHistory userLocationHistory = new UserLocationHistory(location.getLatitude(), location.getLongitude());
                        Call<Void> call = fieldserviceAPI.postUserLocationHistory(idUserFsLogged, userLocationHistory);
                        call.enqueue(new retrofit2.Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Log.i(TAG,"idUserFs: " + idUserFsLogged + " latitude: " + String.valueOf(location.getLatitude()) + " longetude: " + String.valueOf(location.getLongitude()));
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e(TAG, "Erro ao enviar posicao tecnico.", t);
                            }
                        });
                        Log.i(TAG, "Contador: " + count++);
                    }
                };

        handler.sendMessage(new Message());
    }
}