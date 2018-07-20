package br.com.hitss.fieldservicemobile.thread;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.Random;

public class EnviarLocalizacaoRunnable implements Runnable {
    private int delayEnvioLocalizacao;
    private PostOfficeHandlerThread postOfficeHandlerThread;
    private Client.ClientCallback mCallback;
    private Random mRandom;
    private Thread mThread;
    private boolean controller;

    public EnviarLocalizacaoRunnable(PostOfficeHandlerThread postOffice, Client.ClientCallback callback) {
        postOfficeHandlerThread = postOffice;
        mCallback = callback;
        mRandom = new Random(System.currentTimeMillis());
        mThread = new Thread(this);
        controller = true;
    }

    int count = 0;
    public EnviarLocalizacaoRunnable enviarLocalizacao(int millis) {
            try {

                HandlerThread handlerThread = new HandlerThread("MyHandlerThread");
                handlerThread.start();
                Looper looper = handlerThread.getLooper();
                Handler handler =
                        new Handler(looper) {
                            @Override
                            public void handleMessage(Message msg) {
                                Log.i("THREAD", "Contador: " + count++);
                            }
                        };

            } catch (Exception e) {
                e.printStackTrace();
            }
        return this;
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
                postOfficeHandlerThread.sendPost2(new Post());
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
        Client.disposeAll();
    }

    public void setDelayEnvioLocalizacao(int delayEnvioLocalizacao) {
        this.delayEnvioLocalizacao = delayEnvioLocalizacao;
    }
}
