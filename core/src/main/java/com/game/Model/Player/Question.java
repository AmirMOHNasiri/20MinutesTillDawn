package com.game.Model.Player;

public enum Question {
    Q1("What's your first friend's name ?"),
    Q2("What's your school's name ?"),
    Q3("What's your favorite car's brand ?"),
    Q4("What's your favorite color ?");

    private final String question;

    Question(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public static Question getQuestion(String question) {
        for (Question q : values()) {
            if (q.getQuestion().equals(question)) {
                return q;
            }
        }
        return null;
    }
}
