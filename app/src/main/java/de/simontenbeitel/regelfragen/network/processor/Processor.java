package de.simontenbeitel.regelfragen.network.processor;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by Simon on 12.09.2015.
 */
public abstract class Processor {

    protected final Context mContext;

    public Processor(Context context) {
        mContext = context;
    }

    public abstract Bundle execute(Bundle extras);

    public interface Extras {
        /**
         * The provider which the called method is on.
         */
        String PROVIDER_EXTRA = "PROVIDER_EXTRA";

        /**
         * The method to call.
         */
        String METHOD_EXTRA = "METHOD_EXTRA";

        /**
         * The unique request id.
         */
        String REQUEST_ID_EXTRA = "REQUEST_ID_EXTRA";

        /**
         * The Bundle with extras passed to the processor.
         */
        String REQUEST_EXTRAS = "REQUEST_EXTRAS";

        /**
         * Key if operation has completed successfully
         */
        String RESULT_SUCCESSFUL = "result_successful";

        /**
         * The Bundle with extras returned to the requester.
         */
        String RESULT_EXTRAS = "RESULT_EXTRAS";
    }

    public interface Providers {
        int REGELFRAGEN = 1;
    }

    /**
     * Identifier for each provided method.
     * Cannot use 0 as Bundle.getInt(key) returns 0 when the key does not exist.
     */
    public interface Methods {
        int LOAD_QUESTIONS = 1;
    }

}