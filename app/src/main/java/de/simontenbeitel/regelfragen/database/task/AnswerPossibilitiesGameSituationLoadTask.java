package de.simontenbeitel.regelfragen.database.task;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.provider.BaseColumns;

import java.security.InvalidParameterException;

import de.simontenbeitel.regelfragen.database.QuestionDatabase;
import de.simontenbeitel.regelfragen.ui.fragment.GameSituationQuestionFragment;

/**
 * Load all possible answers for the given questionID and all categories of a game situation question
 * 
 * @author Simon Tenbeitel
 */
public class AnswerPossibilitiesGameSituationLoadTask extends AsyncTask<Integer, Void, Cursor[]> {

    private long questionID;
    private GameSituationQuestionFragment fragment;
    private SQLiteDatabase db;

    private static final String tables = QuestionDatabase.Tables.ANSWERPOSSIBILITIES_GAMESITUATION
            + " JOIN " + QuestionDatabase.Tables.ANSWER
            + " ON (" + QuestionDatabase.Tables.ANSWERPOSSIBILITIES_GAMESITUATION + "." + QuestionDatabase.AnswerPossibilitiesGameSituationColumns.ANSWER
            + "=" + QuestionDatabase.Tables.ANSWER + "." + BaseColumns._ID + ")";
    private static final String[] projection = new String[]{QuestionDatabase.Tables.ANSWERPOSSIBILITIES_GAMESITUATION + "." + BaseColumns._ID,
            QuestionDatabase.Tables.ANSWER + "." + QuestionDatabase.AnswerColumns.TEXT};
    private static final String selection = QuestionDatabase.Tables.ANSWERPOSSIBILITIES_GAMESITUATION + "." + QuestionDatabase.AnswerPossibilitiesGameSituationColumns.SERVER + "=?"
            + " AND " + QuestionDatabase.Tables.ANSWERPOSSIBILITIES_GAMESITUATION + "." + QuestionDatabase.AnswerPossibilitiesGameSituationColumns.POSITION + "=?";
    private static final String orderBy = QuestionDatabase.AnswerPossibilitiesGameSituationColumns.ASCENDING_ORDER + " ASC";

    public AnswerPossibilitiesGameSituationLoadTask(long questionID, GameSituationQuestionFragment fragment) {
        this.questionID = questionID;
        this.fragment = fragment;
    }

    @Override
    protected Cursor[] doInBackground(Integer... categories) {
        if (null == categories || 0 == categories.length)
            throw new InvalidParameterException("Must provide at least one category");
        db = new QuestionDatabase(fragment.getActivity()).getReadableDatabase();
        long serverId = getServerIdForQuestion();
        if (0 > serverId)
            throw new InvalidParameterException("Cannot find server for question " + questionID);

        String[] selectionArgs = new String[2];
        selectionArgs[0] = Long.toString(serverId);

        Cursor[] cursors = new Cursor[categories.length];
        for (int index = 0; index < categories.length; index++) {
            selectionArgs[1] = Integer.toString(categories[index]);
            cursors[index] = db.query(tables, projection, selection, selectionArgs, null, null, orderBy);
        }
        return cursors;        
    }

    @Override
    protected void onPostExecute(Cursor[] answerPossibilities) {
        super.onPostExecute(answerPossibilities);
        if (null != fragment)
            fragment.fillSpinner(answerPossibilities);
    }

    private long getServerIdForQuestion() {
        Cursor cursor = db.query(QuestionDatabase.Tables.QUESTION,
                new String[]{QuestionDatabase.QuestionColumns.SERVER},
                BaseColumns._ID + "=?",
                new String[]{Long.toString(questionID)},
                null, null, null);
        return cursor.moveToFirst() ? cursor.getLong(cursor.getColumnIndex(QuestionDatabase.QuestionColumns.SERVER)) : -1L;
    }

}
