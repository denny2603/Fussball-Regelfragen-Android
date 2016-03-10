package de.simontenbeitel.regelfragen.data.remote.dto;

public class QuestionInExamDTO {

    int id;
    int question;
    int exam;
    int position;

    public QuestionInExamDTO() {
    }

    public QuestionInExamDTO(int id, int question, int exam, int position) {
        this.id = id;
        this.question = question;
        this.exam = exam;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestion() {
        return question;
    }

    public void setQuestion(int question) {
        this.question = question;
    }

    public int getExam() {
        return exam;
    }

    public void setExam(int exam) {
        this.exam = exam;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
