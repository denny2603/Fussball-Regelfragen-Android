package de.simontenbeitel.regelfragen.presentation.presenters;

import de.simontenbeitel.regelfragen.domain.model.question.Question;
import de.simontenbeitel.regelfragen.presentation.presenters.base.BasePresenter;

public interface QuestionPresenter extends BasePresenter {

    interface View {
        void displayQuestion(Question question);
    }

}
