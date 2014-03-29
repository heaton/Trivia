package com.adaptionsoft.games.uglytrivia.question;

public enum QuestionCategory {

    POP("Pop"),SCIENCE("Science"),SPORTS("Sports"),ROCK("Rock");

    private String name;

    private QuestionCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
