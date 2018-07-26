package br.com.hitss.fieldservicemobile.thread;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import br.com.hitss.fieldservicemobile.rest.UserRestClient;
import br.com.hitss.fieldservicemobile.util.GPSTracker;

public class EnviarLocalizacaoHandlerThread extends HandlerThread {

    private static final String TAG = EnviarLocalizacaoHandlerThread.class.getSimpleName();

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

        @Override
        protected Void doInBackground(String... params) {
            try {
                new UserRestClient().postUserLocationHistory(params[0], Double.valueOf(params[1]), Double.valueOf(params[2]));
                Log.i(TAG, "idUserFs: "+ params[0] +"  latitude: " + params[1] + "longitude: " + params[2]);
            } catch (Exception e) {
                Log.e(TAG, "Erro ao enviar posicao tecnico.", e);
            }
            return null;
        }
    }

}