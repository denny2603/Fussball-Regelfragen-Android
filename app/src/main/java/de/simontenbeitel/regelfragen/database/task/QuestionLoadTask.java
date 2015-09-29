package de.simontenbeitel.regelfragen.database.task;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.provider.BaseColumns;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import de.simontenbeitel.regelfragen.database.RegelfragenDatabase;
import de.simontenbeitel.regelfragen.objects.GameSituationQuestion;
import de.simontenbeitel.regelfragen.objects.MultipleChoiceQuestion;
import de.simontenbeitel.regelfragen.objects.Question;

/**
 * @author Simon Tenbeitel
 */
public abstract class QuestionLoadTask extends AsyncTask<Long, Void, List<Question>> {

    protected SQLiteDatabase db;
    protected final QuestionLoadCallback mCallback;

    public QuestionLoadTask(QuestionLoadCallback callback) {
        mCallback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        db = RegelfragenDatabase.getInstance().getReadableDatabase();
    }

    @Override
    protected void onPostExecute(List<Question> questions) {
        super.onPostExecute(questions);
        db.close();
        if (null != mCallback) mCallback.onQuestionsLoadFinished(questions);
    }

    protected Question getQuestion(int type, String text, long id) {
        switch (type) {
            case RegelfragenDatabase.QuestionTypeValues.GAMESITUATION:
                return getGameSituationQuestionDetails(text, id);
            case RegelfragenDatabase.QuestionTypeValues.MULTIPLECHOICE:
                return getMultipleChoiceQuestionDetails(text, id);
            default:
                throw new InvalidParameterException("Cannot cast type of question " + type);
        }
    }

    private GameSituationQuestion getGameSituationQuestionDetails(String text, long id) {
        String tables = RegelfragenDatabase.Tables.ANSWER_QUESTION + " JOIN " + RegelfragenDatabase.Tables.ANSWER
                + " ON (" + RegelfragenDatabase.Tables.ANSWER_QUESTION + "." + RegelfragenDatabase.AnswerQuestionColumns.ANSWER + "=" + RegelfragenDatabase.Tables.ANSWER + "." + BaseColumns._ID + ")";
        String[] columns = new String[]{RegelfragenDatabase.Tables.ANSWER_QUESTION + "." + RegelfragenDatabase.AnswerQuestionColumns.POSITION,
                RegelfragenDatabase.Tables.ANSWER + "." + RegelfragenDatabase.AnswerColumns.TEXT};
        String selection = RegelfragenDatabase.Tables.ANSWER_QUESTION + "." + RegelfragenDatabase.AnswerQuestionColumns.QUESTION + "=?";
        String[] selectionArgs = new String[]{Long.toString(id)};
        Cursor cursor = db.query(tables, columns, selection, selectionArgs, null, null, null);
        List<String> restartMethod = new ArrayList<String>();
        List<String> positionOfRestart = new ArrayList<String>();
        List<String> disciplinarySanction = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                String answer = cursor.getString(cursor.getColumnIndex(RegelfragenDatabase.AnswerColumns.TEXT));
                switch (cursor.getInt(cursor.getColumnIndex(RegelfragenDatabase.AnswerQuestionColumns.POSITION))) {
                    case GameSituationQuestion.SpinnerPositions.RESTART_METHOD:
                        restartMethod.add(answer);
                        break;
                    case GameSituationQuestion.SpinnerPositions.POSITION_OF_RESTART:
                        positionOfRestart.add(answer);
                        break;
                    case GameSituationQuestion.SpinnerPositions.DISCIPLINARY_SANCTION:
                        disciplinarySanction.add(answer);
                        break;
                }
            } while (cursor.moveToNext());
        }
        return new GameSituationQuestion(text, id, restartMethod, positionOfRestart, disciplinarySanction);
    }

    private MultipleChoiceQuestion getMultipleChoiceQuestionDetails(String text, long id) {
        String tables = RegelfragenDatabase.Tables.ANSWER_QUESTION + " JOIN " + RegelfragenDatabase.Tables.ANSWER
                + " ON (" + RegelfragenDatabase.Tables.ANSWER_QUESTION + "." + RegelfragenDatabase.AnswerQuestionColumns.ANSWER + "=" + RegelfragenDatabase.Tables.ANSWER + "." + BaseColumns._ID + ")";
        String[] columns = new String[]{RegelfragenDatabase.Tables.ANSWER + "." + RegelfragenDatabase.AnswerColumns.TEXT,
                RegelfragenDatabase.Tables.ANSWER_QUESTION + "." + RegelfragenDatabase.AnswerQuestionColumns.CORRECT};
        String selection = RegelfragenDatabase.Tables.ANSWER_QUESTION + "." + RegelfragenDatabase.AnswerQuestionColumns.QUESTION + "=?";
        String[] selectionArgs = new String[]{Long.toString(id)};
        String orderBy = RegelfragenDatabase.AnswerQuestionColumns.POSITION + " ASC";
        Cursor cursor = db.query(tables, columns, selection, selectionArgs, null, null, orderBy);
        String[] answerPossibilities = new String[cursor.getCount()];
        int solutionIndex = -1;
        if (cursor.moveToFirst()) {
            do {
                String answer = cursor.getString(cursor.getColumnIndex(RegelfragenDatabase.AnswerColumns.TEXT));
                answerPossibilities[cursor.getPosition()] = answer;
                if (RegelfragenDatabase.BooleanValues.TRUE == cursor.getInt(cursor.getColumnIndex(RegelfragenDatabase.AnswerQuestionColumns.CORRECT))) {
                    if (-1 != solutionIndex)
                        throw new RuntimeException("Found more than one correct answer for multiple choice question");
                    solutionIndex = cursor.getPosition();
                }
            } while (cursor.moveToNext());
        }
        if (-1 == solutionIndex)
            throw new RuntimeException("Did not find solution for multiple choice question");
        return new MultipleChoiceQuestion(text, id, answerPossibilities, solutionIndex);
    }

    public interface QuestionLoadCallback {
        void onQuestionsLoadFinished(List<Question> questions);
    }

}
