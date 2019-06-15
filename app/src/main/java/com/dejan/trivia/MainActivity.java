package com.dejan.trivia;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dejan.trivia.data.AnswerListAsyncResponse;
import com.dejan.trivia.data.QuestionBank;
import com.dejan.trivia.model.Question;
import com.dejan.trivia.util.Prefs;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView questionTextview, questionCounterTextview, guessCorrect, recordTextView ;

    private Button trueButton, falseButton, resetButton;
    private ImageButton nextButton, prevButton;
    private int currentQuestionIndex = 0;
    private int bingo = 0;
    private List<Question> questionList;
    private Prefs prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = new Prefs(MainActivity.this);

        resetButton = findViewById(R.id.reset_button);
        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);
        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        questionTextview = findViewById(R.id.question_textView);
        questionCounterTextview = findViewById(R.id.counter_text);
        guessCorrect = findViewById(R.id.guessedCorrectly);
        recordTextView = findViewById(R.id.record_textView);

        recordTextView.setText(String.valueOf(prefs.getHighScore()));
        currentQuestionIndex = prefs.getState();
        bingo = prefs.getScore();

        resetButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);



            questionList = new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                questionTextview.setText(questionArrayList.get(currentQuestionIndex).getAnswer());

               questionCounterTextview.setText(currentQuestionIndex + " / " + questionArrayList.size());
                guessCorrect.setText(""+bingo);
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.prev_button:
                if(currentQuestionIndex>0){
                   currentQuestionIndex = (currentQuestionIndex -1) % questionList.size();
                    updateQuestion();
                }
                break;
            case R.id.next_button:
                changeQuestionIndex();

                break;
            case R.id.true_button:
                checkAnswer(true);
                changeQuestionIndex();

                break;
            case R.id.false_button:
                checkAnswer(false);
                changeQuestionIndex();

                break;
            case R.id.reset_button:
                bingo=0;
                currentQuestionIndex=0;
                recordTextView.setText(String.valueOf(bingo));
                guessCorrect.setText(""+bingo);
                updateQuestion();
                prefs.resetAll();

                break;

        }
    }



    private void checkAnswer(boolean userChooseCorrect) {
        boolean answerIsTrue = questionList.get(currentQuestionIndex).isAnswerTrue();
        int toastMessageId = 0;

        if(userChooseCorrect == answerIsTrue){
            bingo += 1;
            guessCorrect.setText(""+bingo);
            toastMessageId = R.string.correct_answer;
            fadeView();
        }else{
            shakeAnimation();
            toastMessageId = R.string.wrong_answer;
        }
        Toast.makeText(MainActivity.this, toastMessageId, Toast.LENGTH_SHORT).show();
    }

    private void updateQuestion() {
        String question = questionList.get(currentQuestionIndex).getAnswer();
        questionTextview.setText(question);
        questionCounterTextview.setText(currentQuestionIndex + " / " + questionList.size());

    }

    private void changeQuestionIndex(){

            currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
            updateQuestion();

    }
    private void shakeAnimation(){
        Animation shake = AnimationUtils.loadAnimation(
                MainActivity.this, R.anim.shake_animation);
        final CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void fadeView(){
        final CardView cardView = findViewById(R.id.cardView);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onPause() {
        prefs.saveHighScore(bingo);
        prefs.setState(currentQuestionIndex);
        prefs.setScore(bingo);
        super.onPause();


    }
}
