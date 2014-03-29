package com.adaptionsoft.games.uglytrivia.question;

import java.util.Deque;
import java.util.LinkedList;

public class QuestionQueue {

    private QuestionCategory category;
    private Deque<String> list;

    public QuestionQueue(QuestionCategory category) {
        this.category = category;
        list = new LinkedList<String>();
    }

    public String category() {
        return category.getName();
    }

    public void put(String question) {
        list.addLast(question);
    }

    public String pop() {
        return list.removeFirst();
    }

}
