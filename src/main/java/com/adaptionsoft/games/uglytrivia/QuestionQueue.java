package com.adaptionsoft.games.uglytrivia;

import java.util.Deque;
import java.util.LinkedList;

public class QuestionQueue {

    private Deque<String> list;

    public QuestionQueue() {
        list = new LinkedList<String>();
    }

    public void put(String question) {
        list.addLast(question);
    }

    public String pop() {
        return list.removeFirst();
    }
}
