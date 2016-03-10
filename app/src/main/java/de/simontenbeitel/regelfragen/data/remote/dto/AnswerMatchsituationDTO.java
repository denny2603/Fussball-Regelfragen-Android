package de.simontenbeitel.regelfragen.data.remote.dto;

public class AnswerMatchsituationDTO {

    int id;
    int question;
    int answer;

    public AnswerMatchsituationDTO() {
    }

    public AnswerMatchsituationDTO(int id, int question, int answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;
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

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

}
