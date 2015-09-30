package de.simontenbeitel.regelfragen.network.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import de.simontenbeitel.regelfragen.network.processor.Processor;

/**
 * Created by Simon on 12.09.2015.
 */
public abstract class ServiceHelper {

    protected final Context mContext;
    private final int mProviderId;
    private final Set<RequestListener> mListeners = new HashSet<>();
    private final Map<String, Long> mRequests = new HashMap<>();

    protected ServiceHelper(Context context, int providerId) {
        mContext = context;
        mProviderId = providerId;

        // Register broadcast receiver
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(mContext);
        final String action = RegelfragenService.getActionForProviderWithId(providerId);
        broadcastManager.registerReceiver(new RegelfragenServiceBroadcastReceiver(), new IntentFilter(action));
    }

    /**
     * Starts the specified methodId with no parameters
     * @param methodId The method to start
     */
    protected long runMethod(int methodId) {
        return runMethod(methodId, null);
    }

    /**
     * Starts the specified methodId with the parameters given in Bundle
     * @param methodId The method to start
     * @param extras   The parameters to pass to the method
     */
    protected long runMethod(int methodId, Bundle extras) {
        String taskIdentifier = getTaskIdentifier(methodId, extras);
        if (isPending(taskIdentifier))
            return mRequests.get(taskIdentifier);

        long requestId = generateRequestId();
        mRequests.put(taskIdentifier, requestId);

        Intent service = new Intent(mContext, RegelfragenService.class);

        service.putExtra(Processor.Extras.PROVIDER_EXTRA, mProviderId);
        service.putExtra(Processor.Extras.METHOD_EXTRA, methodId);
        service.putExtra(Processor.Extras.REQUEST_ID_EXTRA, requestId);

        if (null != extras) {
            service.putExtra(Processor.Extras.REQUEST_EXTRAS, extras);
        }

        mContext.startService(service);

        return requestId;
    }

    public boolean registerListener(RequestListener listener) {
        return mListeners.add(listener);
    }

    public boolean unregisterListener(RequestListener listener) {
        return mListeners.remove(listener);
    }

    /**
     * Builds a string identifier for this method call.
     * The identifier will contain data about:
     *   What processor was the method called on
     *   What method was called
     *   What parameters were passed
     * This should be enough data to identify a task to detect if a similar task is already running.
     */
    private String getTaskIdentifier(int methodId, Bundle extras)
    {
        Set<String> keySet = extras.keySet();
        String[] keys = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keys);
        StringBuilder identifier = new StringBuilder();

        identifier.append(methodId);
        identifier.append(":");

        for (String key : keys) {
            identifier.append("{");
            identifier.append(key);
            identifier.append(":");
            identifier.append(extras.get(key).toString());
            identifier.append("}");
        }

        return identifier.toString();
    }

    private boolean isPending(String taskIdentifier) {
        return mRequests.containsKey(taskIdentifier);
    }

    private long generateRequestId() {
        return UUID.randomUUID().getLeastSignificantBits();
    }

    private class RegelfragenServiceBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(ServiceHelper.class.getName(), "Received service completed broadcast");
            final long requestId = intent.getLongExtra(Processor.Extras.REQUEST_ID_EXTRA, -1L);
            final Bundle result = intent.getBundleExtra(Processor.Extras.RESULT_EXTRAS);

            mRequests.values().remove(requestId);

            for (RequestListener listener : mListeners) {
                listener.onRequestFinished(requestId, result);
            }
        }
    }

    public interface RequestListener {
        void onRequestFinished(long requestId, Bundle result);
    }

}