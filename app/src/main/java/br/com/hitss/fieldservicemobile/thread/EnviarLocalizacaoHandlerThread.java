package br.com.hitss.fieldservicemobile.thread;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import br.com.hitss.fieldservicemobile.database.GeneratorDataBase;
import br.com.hitss.fieldservicemobile.database.UserLocationHistoryDAO;
import br.com.hitss.fieldservicemobile.model.UserLocationHistory;
import br.com.hitss.fieldservicemobile.util.RetrofitHelper;
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

    private int count = 0;
    public synchronized void sendLocation(final Long idUserFsLogged, final Context context) {

        Handler handler =
                new Handler(getLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        FieldserviceAPI fieldserviceAPI = RetrofitHelper.getInstance().getFieldserviceAPI();
                        GPSTracker gpsTracker = new GPSTracker(context);
                        Location location = gpsTracker.getLocation();
                        final UserLocationHistory userLocationHistory = new UserLocationHistory(location.getLatitude(), location.getLongitude());

                        GeneratorDataBase generatorDataBase = new GeneratorDataBase();
                        final UserLocationHistoryDAO userLocationHistoryDAO = generatorDataBase.generate(context).userLocationHistoryDAO();
                        UserLocationHistory lastUserLocationHistory = userLocationHistoryDAO.getLastUserLocationHistory();
                        if(lastUserLocationHistory == null ||  (!lastUserLocationHistory.getLatitude().equals(userLocationHistory.getLatitude())
                                || !lastUserLocationHistory.getLongitude().equals(userLocationHistory.getLongitude()))){
                            Log.i(TAG,"Location inserida: " + userLocationHistory.getLatitude());
                            userLocationHistoryDAO.insert(userLocationHistory);
                            count++;
                        } else {
                            Log.i(TAG,"NAO INSERIDA: " + userLocationHistory.getLatitude());
                        }
                        if(count >= 20) {
                            Call<Void> call = fieldserviceAPI.postUserLocationHistories(idUserFsLogged, userLocationHistoryDAO.getAll());
                            call.enqueue(new retrofit2.Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        userLocationHistoryDAO.delete(userLocationHistoryDAO.getAll());
                                        count = 0;
                                    } else {
                                        onFailure(call, new Throwable());
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Log.e(TAG, "Erro ao enviar posicao tecnico.", t);
                                }
                            });
                        }

                    }

                };

        handler.sendMessage(new Message());
    }
}