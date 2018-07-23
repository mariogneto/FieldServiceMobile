package br.com.hitss.fieldservicemobile.thread;

import android.content.Context;

public class EnviarLocalizacaoRunnable implements Runnable {

    private int delayEnvioLocalizacao;
    private EnviarLocalizacaoHandlerThread enviarLocalizacaoHandlerThread;
    private Thread mThread;
    private boolean controller;
    private Long idUSerFsLogged;
    private Context context;

    public EnviarLocalizacaoRunnable(EnviarLocalizacaoHandlerThread postOffice, Context context) {
        enviarLocalizacaoHandlerThread = postOffice;
        mThread = new Thread(this);
        controller = true;
        this.context = context;
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
                enviarLocalizacaoHandlerThread.sendLocation(idUSerFsLogged, context);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(delayEnvioLocalizacao);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        controller = false;
    }

    public void setDelayEnvioLocalizacao(int delayEnvioLocalizacao) {
        this.delayEnvioLocalizacao = delayEnvioLocalizacao;
    }

    public Long getIdUSerFsLogged() {
        return idUSerFsLogged;
    }

    public void setIdUSerFsLogged(Long idUSerFsLogged) {
        this.idUSerFsLogged = idUSerFsLogged;
    }

}
