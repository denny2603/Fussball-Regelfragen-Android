package de.simontenbeitel.regelfragen.data.remote.dto;

public class Response {

    QuestionDTO questions;
    AnswerpossibilityMatchsituationDTO answerpossibilities_matchsituation;
    AnswerMultiplechoiceDTO answer_multiplechoice;
    AnswerMatchsituationDTO answer_matchsituation;
    ExamDTO exams;
    QuestionInExamDTO question_in_exam;

    public Response() {
    }

    public Response(QuestionDTO questions, AnswerpossibilityMatchsituationDTO answerpossibilities_matchsituation, AnswerMultiplechoiceDTO answer_multiplechoice, AnswerMatchsituationDTO answer_matchsituation, ExamDTO exams, QuestionInExamDTO question_in_exam) {
        this.questions = questions;
        this.answerpossibilities_matchsituation = answerpossibilities_matchsituation;
        this.answer_multiplechoice = answer_multiplechoice;
        this.answer_matchsituation = answer_matchsituation;
        this.exams = exams;
        this.question_in_exam = question_in_exam;
    }

    public QuestionDTO getQuestions() {
        return questions;
    }

    public void setQuestions(QuestionDTO questions) {
        this.questions = questions;
    }

    public AnswerpossibilityMatchsituationDTO getAnswerpossibilities_matchsituation() {
        return answerpossibilities_matchsituation;
    }

    public void setAnswerpossibilities_matchsituation(AnswerpossibilityMatchsituationDTO answerpossibilities_matchsituation) {
        this.answerpossibilities_matchsituation = answerpossibilities_matchsituation;
    }

    public AnswerMultiplechoiceDTO getAnswer_multiplechoice() {
        return answer_multiplechoice;
    }

    public void setAnswer_multiplechoice(AnswerMultiplechoiceDTO answer_multiplechoice) {
        this.answer_multiplechoice = answer_multiplechoice;
    }

    public AnswerMatchsituationDTO getAnswer_matchsituation() {
        return answer_matchsituation;
    }

    public void setAnswer_matchsituation(AnswerMatchsituationDTO answer_matchsituation) {
        this.answer_matchsituation = answer_matchsituation;
    }

    public ExamDTO getExams() {
        return exams;
    }

    public void setExams(ExamDTO exams) {
        this.exams = exams;
    }

    public QuestionInExamDTO getQuestion_in_exam() {
        return question_in_exam;
    }

    public void setQuestion_in_exam(QuestionInExamDTO question_in_exam) {
        this.question_in_exam = question_in_exam;
    }

}
