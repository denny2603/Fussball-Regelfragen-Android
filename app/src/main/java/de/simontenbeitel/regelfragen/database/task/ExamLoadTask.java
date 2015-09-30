package de.simontenbeitel.regelfragen.database.task;

import java.util.List;

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

        }
        return null;
    }

}
