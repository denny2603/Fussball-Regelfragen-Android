package de.simontenbeitel.regelfragen.domain.repository;

import java.util.Collections;
import java.util.HashSet;

import de.simontenbeitel.regelfragen.domain.model.question.MatchSituationQuestion;
import de.simontenbeitel.regelfragen.domain.model.question.Question;

public class MockQuestionRepositoryImpl implements QuestionRepository {

    @Override
    public Question getRandom() {
        return new MatchSituationQuestion(1,
                "Ein Spieler befindet sich wegen einer Verletzung außerhalb des Spielfelds und beleidigt einen Gegner, der ihn kurz zuvor gefoult hatte. Deshalb verlässt dieser das Feld und stößt den verletzten Spieler heftig zu Boden. Wie ist zu entscheiden, wenn der in unmittelbarer Nähe befindliche SR beide Vorgänge wahrgenommen hat und deshalb das Spiel unterbricht?",
                new HashSet<>(Collections.singleton("Schiedsrichterball")),
                new HashSet<>(Collections.singleton("wo Ball bei Pfiff")),
                new HashSet<>(Collections.singleton("2x Rot")));
    }

    @Override
    public Question getRandomWithout(Long... exclude) {
        return null;
    }

    @Override
    public Question get(long id) {
        return null;
    }

    @Override
    public boolean insert(Question entity) {
        return false;
    }

    @Override
    public boolean update(Question entity) {
        return false;
    }

    @Override
    public boolean delete(Question entity) {
        return false;
    }

}
