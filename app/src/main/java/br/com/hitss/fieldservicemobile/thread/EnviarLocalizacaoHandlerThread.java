package br.com.hitss.fieldservicemobile.thread;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

import br.com.hitss.fieldservicemobile.rest.UserRestClient;
import br.com.hitss.fieldservicemobile.util.GPSTracker;

public class EnviarLocalizacaoHandlerThread extends HandlerThread {

    private static final String TAG = EnviarLocalizacaoHandlerThread.class.getSimpleName();

    private static final int times = 3;

    public EnviarLocalizacaoHandlerThread() {
        super(TAG);
    }

    private int count = 0;
    public synchronized void sendLocation(final Long idUserFsLogged, final Context context) {

        Handler handler =
                new Handler(getLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        EnviaPosicaoTecnicoAsync enviaPosicaoTecnicoAsync = new EnviaPosicaoTecnicoAsync();
                        Log.i(TAG, "AsyncTask Thread: " + Thread.currentThread().getName());
                        GPSTracker gpsTracker = new GPSTracker(context);
                        Location location = gpsTracker.getLocation();
                        enviaPosicaoTecnicoAsync.execute(String.valueOf(idUserFsLogged), String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                        Log.i(TAG, "Contador: " + count++);
                    }
                };

        handler.sendMessage(new Message());
    }

    private static class EnviaPosicaoTecnicoAsync extends AsyncTask<String, Void, Void> {

        private final AtomicInteger counter = new AtomicInteger();

        @Override
        protected Void doInBackground(String... params) {
            try {
                new UserRestClient().postUserLocationHistory(params[0], Double.valueOf(params[1]), Double.valueOf(params[2]));
                Log.i(TAG, "idUserFs: "+ params[0] +"  latitude: " + params[1] + "longitude: " + params[2]);
            } catch (Exception e) {
                while (counter.getAndIncrement() < times) {
                    Log.i(TAG, "Tentativa " + times + "- Reenviando Localizacao idUserFs: " + params[0]);
                    try {
                        Thread.sleep(10000);
                        if (new UserRestClient().postUserLocationHistory(params[0], Double.valueOf(params[1]), Double.valueOf(params[2])))
                            break;
                    } catch (Exception e1) {
                        Log.e(TAG, "Erro ao enviar posicao tecnico.", e);
                    }
                }
            }
            return null;
        }

    }

}