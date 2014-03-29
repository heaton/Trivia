package com.adaptionsoft.games.uglytrivia;

import com.adaptionsoft.games.uglytrivia.question.QuestionCategory;
import com.adaptionsoft.games.uglytrivia.question.QuestionQueue;
import com.adaptionsoft.games.uglytrivia.question.SimpleQuestionFactory;

import java.util.ArrayList;
import java.util.List;

public class QuestionStorage {

    private List<QuestionQueue> list;

    private SimpleQuestionFactory questionFactory = new SimpleQuestionFactory();

    public QuestionStorage(){
        list = new ArrayList<QuestionQueue>();
    }

    public void init(){
        for(QuestionCategory category : QuestionCategory.values()) {
            list.add(questionFactory.create(category));
        }
    }

    public QuestionQueue get(int index) {
        return list.get(index);
    }

    public int size() {
        return list.size();
    }

}
