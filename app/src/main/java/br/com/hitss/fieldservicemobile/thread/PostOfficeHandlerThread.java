package br.com.hitss.fieldservicemobile.thread;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;

public class PostOfficeHandlerThread extends HandlerThread {

    private static final String TAG = PostOfficeHandlerThread.class.getSimpleName();

    private LinkedHashMap<Integer, Handler> mClientDetailsMap;

    public PostOfficeHandlerThread(LinkedHashMap<Integer, Handler> clientDetailsMap) {
        super(TAG);
        mClientDetailsMap = clientDetailsMap;
    }

    private int count = 0;
    public synchronized void register2(final Client client) {

        final WeakReference<Client> clientWeakReference = new WeakReference<Client>(client);

        Handler handler =
                new Handler(getLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        Log.i("THREAD", "Contador: " + count++);
                    }
                };
        mClientDetailsMap.put(client.getId(), handler);
    }

    public synchronized void sendPost2(Post post) throws InvalidRequestException, NotRegisteredException {

        Handler handler =
                new Handler(getLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        Log.i("THREAD", "Contador: " + count++);
                    }
                };

        handler.sendMessage(new Message());
    }

    public static class InvalidRequestException extends Exception {
        public InvalidRequestException(String message) {
            super(message);
        }
    }

    public static class AlreadyExistsException extends Exception {
        public AlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class NotRegisteredException extends Exception {
        public NotRegisteredException(String message) {
            super(message);
        }
    }
}