package com.adaptionsoft.games.uglytrivia;

import java.util.Deque;
import java.util.LinkedList;

public class QuestionQueue {

    private String category;
    private Deque<String> list;

    public QuestionQueue(String category) {
        this.category = category;
        list = new LinkedList<String>();
    }

    public String category() {
        return category;
    }

    public void put(String question) {
        list.addLast(question);
    }

    public String pop() {
        return list.removeFirst();
    }

}
