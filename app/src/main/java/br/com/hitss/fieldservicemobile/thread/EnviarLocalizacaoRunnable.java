package br.com.hitss.fieldservicemobile.thread;

import android.content.Context;
import android.util.Log;

public class EnviarLocalizacaoRunnable implements Runnable {

    private static final String TAG = EnviarLocalizacaoRunnable.class.getSimpleName();

    private int delayEnvioLocalizacao;
    private final EnviarLocalizacaoHandlerThread enviarLocalizacaoHandlerThread;
    private final Thread mThread;
    private boolean controller;
    private Long idUSerFsLogged;
    private final Context context;

    public EnviarLocalizacaoRunnable(EnviarLocalizacaoHandlerThread enviarLocalizacaoHandlerThread, Context context) {
        this.enviarLocalizacaoHandlerThread = enviarLocalizacaoHandlerThread;
        this.context = context;
        mThread = new Thread(this);
        controller = true;
    }

    public synchronized void start() {
        if (!mThread.isAlive())
            mThread.start();
    }

    @Override
    public void run() {
        controller = true;
        while (controller) {
            try {
                Log.i(TAG, "Enviando Localizacao idUserFs: " + idUSerFsLogged);
                enviarLocalizacaoHandlerThread.sendLocation(idUSerFsLogged, context);
            } catch (Exception e) {
                Log.e(TAG, "Erro ao enviar Localizacao idUSerFs: " + idUSerFsLogged, e);
            }
            try {
                Thread.sleep(delayEnvioLocalizacao);
            } catch (InterruptedException e) {
                Log.e(TAG, "Erro dormir Thread idUSerFs: " + idUSerFsLogged, e);
            }
        }
    }

    public void stop() {
        controller = false;
    }

    public void setDelayEnvioLocalizacao(int delayEnvioLocalizacao) {
        this.delayEnvioLocalizacao = delayEnvioLocalizacao;
    }

     public void setIdUSerFsLogged(Long idUSerFsLogged) {
        this.idUSerFsLogged = idUSerFsLogged;
    }

}
