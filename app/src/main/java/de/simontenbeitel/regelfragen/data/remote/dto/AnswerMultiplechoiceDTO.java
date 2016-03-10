package de.simontenbeitel.regelfragen.data.remote.dto;

public class AnswerMultiplechoiceDTO {

    int id;
    String text;
    int question;
    int correct;

    public AnswerMultiplechoiceDTO() {
    }

    public AnswerMultiplechoiceDTO(int id, String text, int question, int correct) {
        this.id = id;
        this.text = text;
        this.question = question;
        this.correct = correct;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getQuestion() {
        return question;
    }

    public void setQuestion(int question) {
        this.question = question;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

}
