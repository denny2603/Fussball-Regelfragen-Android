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

    private final int numberOfQuestionsToLoadEachTime;

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
        String selection = BaseColumns._ID + " NOT IN (" + RegelfragenDatabase.makePlaceholders(loadedQuestions.length) + ")";
        Set<String> answeredQuestionsString = new HashSet<String>(loadedQuestions.length);
        for(Long id : loadedQuestions)
            answeredQuestionsString.add(Long.toString(id));
        String[] selectionArgs = answeredQuestionsString.toArray(new String[answeredQuestionsString.size()]);
        Cursor questionCursor = db.query(RegelfragenDatabase.Tables.QUESTION, projection, selection, selectionArgs, null, null, "RANDOM() LIMIT " + numberOfQuestionsToLoadEachTime);
        List<Question> questions = new ArrayList<>(5);
        if (questionCursor.moveToFirst()) {
            do {
                long id = questionCursor.getLong(questionCursor.getColumnIndex(BaseColumns._ID));
                String text = questionCursor.getString(questionCursor.getColumnIndex(RegelfragenDatabase.QuestionColumns.TEXT));
                int type = questionCursor.getInt(questionCursor.getColumnIndex(RegelfragenDatabase.QuestionColumns.TYPE));
                questions.add(getQuestion(type, text, id));
            } while (questionCursor.moveToNext());
            questionCursor.close();
        }
        return questions;
    }

}
