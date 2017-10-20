package edu.orangecoastcollege.cs273.sbadajoz.cs273superheroes;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    public static final String QUIZ_TYPE = "pref_quizType";
    private static final String TAG = "Superhero Quiz";
    private static final int SUPERHEROES_IN_QUIZ = 10;
    private Button[] mButtons = new Button[4];
    private List<SuperHero> mAllSuperHeroes;
    private List<SuperHero> mQuizSuperheroes;
    private SuperHero mCorrect;
    private int mTotalGuesses;
    private int mCorrectGuesses;
    private SecureRandom rng;
    private Handler handler;
    private TextView mQuestionNumberTextView;
    private ImageView mImageView;
    private TextView mAnswerTextView;
    private TextView mQuestionTextView;
    private String mQuizType;
    private String mCorrectText;
    /**
     * A callback to be invoked when a shared preference is changed.
     */
    SharedPreferences.OnSharedPreferenceChangeListener mPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        /**
         * Called when a shared preference is changed, added, or removed. This may be called even if a preference is set to its existing value.
         *
         * @param sharedPreferences The SharedPreferences that received the change.
         * @param key The key of the preference that was changed, added, or removed.
         */
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            mQuizType = sharedPreferences.getString(QUIZ_TYPE, "Superhero Name");
            resetQuiz();

            Toast.makeText(QuizActivity.this, R.string.restarting_quiz, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), QuizActivity.class));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(mPreferenceChangeListener);

        mQuizSuperheroes = new ArrayList<>(SUPERHEROES_IN_QUIZ);
        rng = new SecureRandom();
        handler = new Handler();

        mQuestionNumberTextView = (TextView) findViewById(R.id.questionNumberTextView);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mAnswerTextView = (TextView) findViewById(R.id.answerTextView);
        mQuestionTextView = (TextView) findViewById(R.id.questionTextView);

        mButtons[0] = (Button) findViewById(R.id.button);
        mButtons[1] = (Button) findViewById(R.id.button2);
        mButtons[2] = (Button) findViewById(R.id.button3);
        mButtons[3] = (Button) findViewById(R.id.button4);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mQuizType = preferences.getString(QUIZ_TYPE, "Superhero Name");

        mQuestionNumberTextView.setText(getString(R.string.question, 0, SUPERHEROES_IN_QUIZ));
        try {
            mAllSuperHeroes = JSONLoader.loadJSONFromAsset(this);

            resetQuiz();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up and starts a new quiz.
     */
    public void resetQuiz() {
        mCorrectGuesses = 0;
        mTotalGuesses = 0;
        mQuizSuperheroes.clear();

        mQuestionTextView.setText(getString(R.string.guess_country, mQuizType));

        boolean notAdded = true;
        for (int i = 0; i < SUPERHEROES_IN_QUIZ; i++) {
            notAdded = true;
            while (notAdded) {
                SuperHero superhero = mAllSuperHeroes.get(rng.nextInt(mAllSuperHeroes.size()));
                if (!mQuizSuperheroes.contains(superhero)) {
                    mQuizSuperheroes.add(superhero);
                    notAdded = false;
                }
            }
        }

        loadNextSuperhero();
    }

    /**
     * Method initiates the process of loading the next superhero for the quiz, showing
     * the superhero's image and then 4 buttons, one of which contains the correct answer.
     */
    private void loadNextSuperhero() {
        mCorrect = mQuizSuperheroes.remove(0);
        mAnswerTextView.setText("");
        mQuestionNumberTextView.setText(getString(R.string.question, mCorrectGuesses + 1, SUPERHEROES_IN_QUIZ));

        AssetManager am = getAssets();

        try {
            InputStream inputStream = am.open(mCorrect.getFileName());
            Drawable superhero = Drawable.createFromStream(inputStream, mCorrect.getName());
            mImageView.setImageDrawable(superhero);
            Collections.shuffle(mAllSuperHeroes);
            while (mAllSuperHeroes.subList(0, mButtons.length).contains(mCorrect)) Collections.shuffle(mAllSuperHeroes);
            for (int i = 0; i < mButtons.length; i++) {
                String mButtonText;
                switch (mQuizType) {
                    case ("Superhero Name"):
                        mButtonText = mAllSuperHeroes.get(i).getName();
                        break;
                    case ("Superpower"):
                        mButtonText = mAllSuperHeroes.get(i).getSuperpower();
                        break;
                    default:
                        mButtonText = mAllSuperHeroes.get(i).getOneThing();
                        break;
                }
                mButtons[i].setText(mButtonText);
                mButtons[i].setEnabled(true);
            }
            switch (mQuizType) {
                case ("Superhero Name"):
                    mCorrectText = mCorrect.getName();
                    break;
                case ("Superpower"):
                    mCorrectText = mCorrect.getSuperpower();
                    break;
                default:
                    mCorrectText = mCorrect.getOneThing();
                    break;
            }
            mButtons[rng.nextInt(mButtons.length)].setText(mCorrectText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the click event of one of the 4 buttons indicating the guess of a country's name
     * to match the superhero image displayed.  If the guess is correct, the country's name (in GREEN) will be shown,
     * followed by a slight delay of 2 seconds, then the next superhero will be loaded.  Otherwise, the
     * word "Incorrect Guess" will be shown in RED and the button will be disabled.
     *
     * @param v
     */
    public void makeGuess(View v) {
        Button guessedSuperhero = (Button) v;
        String guess = guessedSuperhero.getText().toString();

        if (guess.equals(mCorrectText)) {
            mCorrectGuesses++;
            mTotalGuesses++;
            mAnswerTextView.setTextColor(ContextCompat.getColor(this, R.color.correct_answer));
            mAnswerTextView.setText(guess);
            for (Button mButton : mButtons) {
                mButton.setEnabled(false);
            }
            if (mCorrectGuesses == 10) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                Log.i(TAG, "Correct guesses: " + mCorrectGuesses + ", Total Guesses: " + mTotalGuesses + ", Answer: " + ((float) (mCorrectGuesses) / (float) (mTotalGuesses)));
                builder.setMessage(getString(R.string.results, mTotalGuesses, 100.0 * ((float) (mCorrectGuesses) / (float) (mTotalGuesses))));
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.reset_quiz, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetQuiz();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        loadNextSuperhero();
                    }
                };
                handler.postDelayed(runnable, 1200);
            }
        }

        else {
            mAnswerTextView.setTextColor(ContextCompat.getColor(this, R.color.incorrect_answer));
            mAnswerTextView.setText(getString(R.string.incorrect_answer));
            guessedSuperhero.setEnabled(false);
            mTotalGuesses++;
        }
    }

    /**
     * Called on the creation of the options menu
     * Inflates the created view
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed; if you return false it will not be shown.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal processing happen
     * (calling the item's Runnable or sending a message to its Handler as appropriate).
     * Launches a content to {@link SettingsActivity}
     *
     * @param item The menu item that was selected.
     * @return Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, SettingsActivity.class));
        return super.onOptionsItemSelected(item);
    }
}
