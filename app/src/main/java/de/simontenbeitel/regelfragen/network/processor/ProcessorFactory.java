package de.simontenbeitel.regelfragen.network.processor;

import android.content.Context;

import java.security.InvalidParameterException;

/**
 * Created by Simon on 12.09.2015.
 */
public class ProcessorFactory {

    public static Processor getProcessor(Context context, int methodId) {
        switch (methodId) {
            case Processor.Methods.LOAD_QUESTIONS:
                return new LoadQuestionsProcessor(context);
            default:
                throw new InvalidParameterException("Can't resolve method id " + methodId);
        }
    }

}
