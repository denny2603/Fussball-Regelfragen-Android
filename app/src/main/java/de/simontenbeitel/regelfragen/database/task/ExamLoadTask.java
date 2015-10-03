package de.simontenbeitel.regelfragen.database.task;

import android.database.Cursor;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import de.simontenbeitel.regelfragen.RegelfragenApplication;
import de.simontenbeitel.regelfragen.database.RegelfragenContract;
import de.simontenbeitel.regelfragen.database.RegelfragenDatabase;
import de.simontenbeitel.regelfragen.objects.Question;

/**
 * @author Simon Tenbeitel
 */
public class ExamLoadTask extends QuestionLoadTask{

    public ExamLoadTask(QuestionLoadCallback callback) {
        super(callback);
    }

    @Override
    protected List<Question> doInBackground(Long... params) {
        long examId;
        if (1 == params.length) {
            examId = params[0];
        } else {
            Cursor possibleExamIdsCursor = RegelfragenDatabase.getPossibleExams(new String[]{BaseColumns._ID});
            if (possibleExamIdsCursor.moveToFirst()) {
                int cursorPosition = RegelfragenApplication.getRandom().nextInt(possibleExamIdsCursor.getCount());
                possibleExamIdsCursor.moveToPosition(cursorPosition);
                examId = possibleExamIdsCursor.getLong(possibleExamIdsCursor.getColumnIndex(BaseColumns._ID));
                possibleExamIdsCursor.close();
            } else {
                possibleExamIdsCursor.close();
                return null;
            }
        }
        return getQuestions(examId);
    }

    protected List<Question> getQuestions(long examId) {
        String table = RegelfragenDatabase.Tables.QUESTION + " as q JOIN " + RegelfragenDatabase.Tables.QUESTION_IN_EXAM + " as qie ON (q." + BaseColumns._ID + " = qie." + RegelfragenContract.QuestionInExam.Columns.QUESTION + ")";
        String[] projection = new String[] {"q." + BaseColumns._ID,
                "q." + RegelfragenDatabase.QuestionColumns.TEXT,
                "q." + RegelfragenDatabase.QuestionColumns.TYPE};
        String selection = "qie." + RegelfragenContract.QuestionInExam.Columns.EXAM + "=?";
        String[] selectionArgs = new String[] {Long.toString(examId)};
        Cursor questionsCursor = db.query(table, projection, selection, selectionArgs, null, null, "qie." + RegelfragenContract.QuestionInExam.Columns.POSITION + " ASC");

        List<Question> questions = new ArrayList<>(questionsCursor.getCount());
        if (questionsCursor.moveToFirst()) {
            do {
                long id = questionsCursor.getLong(questionsCursor.getColumnIndex(BaseColumns._ID));
                String text = questionsCursor.getString(questionsCursor.getColumnIndex(RegelfragenDatabase.QuestionColumns.TEXT));
                int type = questionsCursor.getInt(questionsCursor.getColumnIndex(RegelfragenDatabase.QuestionColumns.TYPE));
                questions.add(getQuestion(type, text, id));
            } while (questionsCursor.moveToNext());
            questionsCursor.close();
        }
        return questions;
    }

}
