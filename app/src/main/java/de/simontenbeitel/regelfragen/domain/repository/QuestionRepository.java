package de.simontenbeitel.regelfragen.domain.repository;

import de.simontenbeitel.regelfragen.domain.model.question.Question;

/**
 * A repository with CRUD operations on the question model.
 */
public interface QuestionRepository extends Repository<Question>{

    Question getRandom();

    Question getRandomWithout(Long... exclude);

}
