package ma.snrt.topquiz.controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Arrays;

import ma.snrt.topquiz.R;
import ma.snrt.topquiz.model.Question;
import ma.snrt.topquiz.model.QuestionBank;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mQuestionTxt;
    private Button mAnswer1Btn;
    private Button mAnswer2Btn;
    private Button mAnswer3Btn;
    private Button mAnswer4Btn;

    private QuestionBank mQuestionBank;
    private Question mCurrentQuestion;

   private static final String TAG = "GameActivity";

    private int mScore;
    private int mNumberOfQuestions;

    public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE";
    public static final String BUNDLE_STATE_SCORE = "currentScore";
    public static final String BUNDLE_STATE_QUESTION = "currentQuestion";
    public static final String BUNDLE_STATE_QUESTION_BANK = "currentQuestionBank";

    private boolean mEnableTouchEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if (savedInstanceState != null) {
            mScore = savedInstanceState.getInt(BUNDLE_STATE_SCORE);
            mNumberOfQuestions = savedInstanceState.getInt(BUNDLE_STATE_QUESTION);

            Log.i(TAG, "Score : "+mScore);
            Log.i(TAG, "NumberOfQuestions : "+mNumberOfQuestions);

            String jsonQuestionBank= savedInstanceState.getString(BUNDLE_STATE_QUESTION_BANK);
            if(!jsonQuestionBank.isEmpty()) {
                Gson gson = new Gson();
                mQuestionBank = gson.fromJson(jsonQuestionBank, QuestionBank.class);
            }
        } else {
            mQuestionBank = this.generateQuestions();
            mScore = 0;
            mNumberOfQuestions = 4;
        }

        mEnableTouchEvents = true;

        mQuestionTxt = findViewById(R.id.activity_game_question_txt);
        mAnswer1Btn = findViewById(R.id.activity_game_answer1_btn);
        mAnswer2Btn = findViewById(R.id.activity_game_answer2_btn);
        mAnswer3Btn = findViewById(R.id.activity_game_answer3_btn);
        mAnswer4Btn = findViewById(R.id.activity_game_answer4_btn);

        // Use the tag property to 'name' the buttons
        mAnswer1Btn.setTag(0);
        mAnswer2Btn.setTag(1);
        mAnswer3Btn.setTag(2);
        mAnswer4Btn.setTag(3);

        mAnswer1Btn.setOnClickListener(this);
        mAnswer2Btn.setOnClickListener(this);
        mAnswer3Btn.setOnClickListener(this);
        mAnswer4Btn.setOnClickListener(this);

        mCurrentQuestion = mQuestionBank.getQuestion();
        this.displayQuestion(mCurrentQuestion);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putInt(BUNDLE_STATE_SCORE, mScore);
        outState.putInt(BUNDLE_STATE_QUESTION, mNumberOfQuestions);

        Gson gson = new Gson();
        String jsonQuestionBank = gson.toJson(mQuestionBank);
        outState.putString(BUNDLE_STATE_QUESTION_BANK, jsonQuestionBank);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v){
        int responseIndex = (int) v.getTag();
        mEnableTouchEvents = false;
        if (responseIndex == mCurrentQuestion.getAnswerIndex()) {
            // Good answer
            mScore++;
            Toast.makeText(this, "Correct answer "+mScore, Toast.LENGTH_SHORT).show();
        } else {
            // Wrong answer
            Toast.makeText(this, "Wrong answer!", Toast.LENGTH_SHORT).show();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run(){
                mEnableTouchEvents = true;
                if(--mNumberOfQuestions <= 0) {
                    // End of the game
                    endGame();
                }else {
                    // Display another question
                    mCurrentQuestion = mQuestionBank.getQuestion();
                    displayQuestion(mCurrentQuestion);
                }
            }
        }, 2000);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        return mEnableTouchEvents && super.dispatchTouchEvent(ev);
    }

    private void endGame() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        mEnableTouchEvents = false;
        builder.setTitle("Well done!")
            .setMessage("Your score is " + mScore)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.putExtra(BUNDLE_EXTRA_SCORE, mScore);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }).create().show();
    }
    private void displayQuestion(final Question question) {
        mQuestionTxt.setText(question.getQuestion());
        mAnswer1Btn.setText(question.getChoiceList().get(0));
        mAnswer2Btn.setText(question.getChoiceList().get(1));
        mAnswer3Btn.setText(question.getChoiceList().get(2));
        mAnswer4Btn.setText(question.getChoiceList().get(3));
    }

    private QuestionBank generateQuestions() {
        Question question1 = new Question("What is the name of the current french president?",
                Arrays.asList("François Hollande", "Emmanuel Macron", "Jacques Chirac", "François Mitterand"),
                1);

        Question question2 = new Question("How many countries are there in the European Union?",
                Arrays.asList("15", "24", "28", "32"),
                2);

        Question question3 = new Question("Who is the creator of the Android operating system?",
                Arrays.asList("Andy Rubin", "Steve Wozniak", "Jake Wharton", "Paul Smith"),
                0);

        Question question4 = new Question("When did the first man land on the moon?",
                Arrays.asList("1958", "1962", "1967", "1969"),
                3);

        Question question5 = new Question("What is the capital of Romania?",
                Arrays.asList("Bucarest", "Warsaw", "Budapest", "Berlin"),
                0);

        Question question6 = new Question("Who did the Mona Lisa paint?",
                Arrays.asList("Michelangelo", "Leonardo Da Vinci", "Raphael", "Carravagio"),
                1);

        Question question7 = new Question("In which city is the composer Frédéric Chopin buried?",
                Arrays.asList("Strasbourg", "Warsaw", "Paris", "Moscow"),
                2);

        Question question8 = new Question("What is the country top-level domain of Belgium?",
                Arrays.asList(".bg", ".bm", ".bl", ".be"),
                3);

        Question question9 = new Question("What is the house number of The Simpsons?",
                Arrays.asList("42", "101", "666", "742"),
                3);

        return new QuestionBank(Arrays.asList(
                question1,
                question2,
                question3,
                question4,
                question5,
                question6,
                question7,
                question8,
                question9
            )
        );
    }
}
