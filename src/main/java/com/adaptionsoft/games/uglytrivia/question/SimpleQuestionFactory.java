package com.adaptionsoft.games.uglytrivia.question;

public class SimpleQuestionFactory {

    public QuestionQueue create(QuestionCategory category) {
        QuestionQueue questions = new QuestionQueue(category);
        for (int i = 0; i < 50; i++) {
            questions.put(category.getName() + " Question " + i);
        }
        return questions;
    }

}
