package com.dejan.trivia.data;

import com.dejan.trivia.model.Question;

import java.util.ArrayList;

public interface AnswerListAsyncResponse {
     void processFinished(ArrayList<Question> questionArrayList);
}
