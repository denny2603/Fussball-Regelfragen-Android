package de.simontenbeitel.regelfragen.database.task;

import android.database.Cursor;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.simontenbeitel.regelfragen.database.QuestionDatabase;
import de.simontenbeitel.regelfragen.objects.Question;

/**
 * @author Simon Tenbeitel
 */
public class SingleQuestionsLoadTask extends QuestionLoadTask {

    public SingleQuestionsLoadTask(QuestionLoadTask.QuestionLoadCallback callback) {
        super(callback);
    }

    @Override
    protected List<Question> doInBackground(Long... params) {
        return getSingleQuestions(params);
    }

    private List<Question> getSingleQuestions(Long... answeredQuestions) {
        String[] projection = new String[] {BaseColumns._ID, QuestionDatabase.QuestionColumns.TEXT, QuestionDatabase.QuestionColumns.TYPE};
        String selection = BaseColumns._ID + " NOT IN (" + makePlaceholders(answeredQuestions.length) + ")";
        Set<String> answeredQuestionsString = new HashSet<String>(answeredQuestions.length);
        for(Long id : answeredQuestions)
            answeredQuestionsString.add(Long.toString(id));
        String[] selectionArgs = answeredQuestionsString.toArray(new String[answeredQuestionsString.size()]);
        Cursor fragenCursor = db.query(QuestionDatabase.Tables.QUESTION, projection, selection, selectionArgs, null, null, "RANDOM() LIMIT 5");
        List<Question> questions = new ArrayList<>(5);
        if (fragenCursor.moveToFirst()) {
            do {
                long id = fragenCursor.getLong(fragenCursor.getColumnIndex(BaseColumns._ID));
                String text = fragenCursor.getString(fragenCursor.getColumnIndex(QuestionDatabase.QuestionColumns.TEXT));
                int type = fragenCursor.getInt(fragenCursor.getColumnIndex(QuestionDatabase.QuestionColumns.TYPE));
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
