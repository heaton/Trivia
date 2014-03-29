package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.List;

public class QuestionStorage {

    private List<QuestionQueue> list;

    public QuestionStorage(){
        list = new ArrayList<QuestionQueue>();
    }

    public void init(){
        list.add(createQuestions(POP));
        list.add(createQuestions(SCIENCE));
        list.add(createQuestions(SPORTS));
        list.add(createQuestions(ROCK));
    }

    private QuestionQueue createQuestions(String category){
        QuestionQueue questions = new QuestionQueue(category);
        for (int i = 0; i < 50; i++) {
            questions.put(category + " Question " + i);
        }
        return questions;
    }

    public QuestionQueue get(int index) {
        return list.get(index);
    }

    public int size() {
        return list.size();
    }

    private static final String POP = "Pop";
    private static final String SCIENCE = "Science";
    private static final String SPORTS = "Sports";
    private static final String ROCK = "Rock";

}
