package edu.orangecoastcollege.cs273.sbadajoz.cs273superheroes;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.security.SecureRandom;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "Flag Quiz";

    private static final int FLAGS_IN_QUIZ = 10;

    private Button[] mButtons = new Button[4];
    private List<SuperHero> mAllSuperHeroes;
    private List<SuperHero> mQuizSuperHeroes;
    private SuperHero mCorrect;
    private int mTotalGuesses;
    private int mCorrectGuesses;
    private SecureRandom rng;
    private Handler handler;

    private TextView mQuestionNumberTextView;
    private ImageView mImageView;
    private TextView mAnswerTextView;

    private int mChoices;
    private String mRegion;

    public static final String QUIZ_TYPE = "pref_quizType";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

    }
}
