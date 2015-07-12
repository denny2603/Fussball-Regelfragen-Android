package de.simontenbeitel.regelfragen.database.task;

import android.database.Cursor;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.simontenbeitel.regelfragen.database.RegelfragenDatabase;
import de.simontenbeitel.regelfragen.objects.Question;

/**
 * @author Simon Tenbeitel
 */
public class SingleQuestionsLoadTask extends QuestionLoadTask {

    private int numberOfQuestionsToLoadEachTime;

    public SingleQuestionsLoadTask(QuestionLoadTask.QuestionLoadCallback callback, int numberOfQuestionsToLoadEachTime) {
        super(callback);
        this.numberOfQuestionsToLoadEachTime = numberOfQuestionsToLoadEachTime;
    }

    @Override
    protected List<Question> doInBackground(Long... params) {
        return getSingleQuestions(params);
    }

    private List<Question> getSingleQuestions(Long... loadedQuestions) {
        String[] projection = new String[] {BaseColumns._ID, RegelfragenDatabase.QuestionColumns.TEXT, RegelfragenDatabase.QuestionColumns.TYPE};
        String selection = BaseColumns._ID + " NOT IN (" + makePlaceholders(loadedQuestions.length) + ")";
        Set<String> answeredQuestionsString = new HashSet<String>(loadedQuestions.length);
        for(Long id : loadedQuestions)
            answeredQuestionsString.add(Long.toString(id));
        String[] selectionArgs = answeredQuestionsString.toArray(new String[answeredQuestionsString.size()]);
        Cursor fragenCursor = db.query(RegelfragenDatabase.Tables.QUESTION, projection, selection, selectionArgs, null, null, "RANDOM() LIMIT " + numberOfQuestionsToLoadEachTime);
        List<Question> questions = new ArrayList<>(5);
        if (fragenCursor.moveToFirst()) {
            do {
                long id = fragenCursor.getLong(fragenCursor.getColumnIndex(BaseColumns._ID));
                String text = fragenCursor.getString(fragenCursor.getColumnIndex(RegelfragenDatabase.QuestionColumns.TEXT));
                int type = fragenCursor.getInt(fragenCursor.getColumnIndex(RegelfragenDatabase.QuestionColumns.TYPE));
                questions.add(getQuestion(type, text, id));
            } while (fragenCursor.moveToNext());
        }
        return questions;
    }

    private static String makePlaceholders(int length) {
        if(length <= 0) return "";
        StringBuilder sb = new StringBuilder(length * 2 - 1);
        sb.append("?");
        for (int i = 1; i < length; i++) {
            sb.append(",?");
        }
        return sb.toString();
    }

}
