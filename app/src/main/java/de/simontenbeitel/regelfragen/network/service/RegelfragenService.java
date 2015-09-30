package de.simontenbeitel.regelfragen.network.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.security.InvalidParameterException;

import de.simontenbeitel.regelfragen.network.processor.Processor;
import de.simontenbeitel.regelfragen.network.processor.ProcessorFactory;

/**
 * Created by Simon on 12.09.2015.
 */
public class RegelfragenService extends IntentService {

    public static final String ACTION = "de.simontenbeitel.regelfragen.service.completed";

    public RegelfragenService() {
        super(RegelfragenService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(RegelfragenService.class.getName(), "Service started");
        final int methodId = intent.getIntExtra(Processor.Extras.METHOD_EXTRA, -1);
        if (0 > methodId)
            throw new InvalidParameterException("No or invalid method id passed");
        final Bundle extras = intent.getBundleExtra(Processor.Extras.REQUEST_EXTRAS);

        // Get processor and execute request
        final Processor processor = ProcessorFactory.getProcessor(getApplicationContext(), methodId);
        Bundle result = processor.execute(extras);

        // Prepare intent to be sent via broadcast manager
        int providerId = intent.getIntExtra(Processor.Extras.PROVIDER_EXTRA, -1);
        Intent sendCompletedIntent = new Intent();
        sendCompletedIntent.setAction(getActionForProviderWithId(providerId));
        long requestId = intent.getLongExtra(Processor.Extras.REQUEST_ID_EXTRA, -1L);
        sendCompletedIntent.putExtra(Processor.Extras.REQUEST_ID_EXTRA, requestId);
        sendCompletedIntent.putExtra(Processor.Extras.RESULT_EXTRAS, result);

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.sendBroadcast(sendCompletedIntent);
    }

    public static String getActionForProviderWithId(int providerId) {
        return ACTION + "." + providerId;
    }

}